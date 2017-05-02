package nju.software.sjjh.bank.service;

import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.bank.dao.ErrorQueueBankDao;
import nju.software.sjjh.bank.dao.QueueBankDao;
import nju.software.sjjh.bank.entity.QueueBank;
import nju.software.sjjh.bank.model.Result;
import nju.software.sjjh.bank.model.SendRequestModel;
import nju.software.sjjh.bank.model.SendResponseModel;
import nju.software.sjjh.dao.CodeConfigDao;
import nju.software.sjjh.entity.CodeConfig;
import nju.software.sjjh.util.CollectionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * 银行定时任务业务逻辑实现
 * Created by Nekonekod on 2017/4/24.
 */
@Service
@Slf4j
public class BankScheduleServiceImpl implements BankScheduleService {

    /**
     * 线程池
     */
    private ExecutorService threadPool = Executors.newFixedThreadPool(5);

    @Resource
    private QueueBankDao queueBankDao;

    @Resource
    private ErrorQueueBankDao errorQueueBankDao;

    @Resource
    private BankErrorHandlerScheduleService bankErrorHandlerScheduleService;

    @Resource
    private CodeConfigDao codeConfigDao;

    @Override
    public void sendRequest(int priority) {
        long startTime = System.currentTimeMillis();
        List<CodeConfig> banks = codeConfigDao.getBanks();
        for (CodeConfig bank:banks) {
            String replier = bank.getCodeKey();   //协调公司标识
            String wsAddress = bank.getCodeValue(); //ws地址
            //获取所有待发送给银行的请求
            List<QueueBank> requests = queueBankDao.getRequestsForSendingRequest(
                    replier, BankService.STATUS_RECEIVE_REQUEST, priority);
            //提交任务
            if(CollectionUtil.isNotEmpty(requests)){
                Future<List<SendRequestModel>> future = threadPool.submit(new SendRequestModel.SendRequestCallback("send to replier：" + replier, wsAddress, requests));
                try {
                    List<SendRequestModel> models = future.get();
                    for(SendRequestModel model:models){
                        Result result = model.getResult();
                        List<QueueBank> toUpdate = model.getRequests();
                        //判断是否接收成功
                        if(result!=null && result.getValue()!=null && result.getValue()==1){
                            //成功：更新请求
                            //获取主键集合
                            List<String> uuids = CollectionUtil.mapping(toUpdate, new CollectionUtil.MappingCallback<QueueBank, String>() {
                                public String map(QueueBank queueBank) {
                                    return queueBank.getUuid();
                                }
                            });
                            queueBankDao.updateForSendRequest(BankService.STATUS_SEND_REQUEST,model.getResponseId(),new Date(),uuids);
                        }else{
                            //失败：添加到错误队列
                            String errorMessage = model.getResult() != null ? model.getResult().getMessage() : "未响应成功";
                            bankErrorHandlerScheduleService.addToErrorQueue(toUpdate,errorMessage);
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("sendRequest("+priority+") cost time " + (endTime-startTime) + "ms");
    }

    /**
     * 错误重发成功后恢复请求队列记录的状态
     * @param responseId
     * @param uuids
     */
    @Override
    @Transactional
    public void recoverSendRequest(String responseId, List<String> uuids){
        //更新请求队列表
        queueBankDao.updateForSendRequest(BankService.STATUS_SEND_REQUEST,responseId,new Date(),uuids);
        //更新错误队列表
        errorQueueBankDao.updateForResendRequest(BankService.STATUS_SEND_REQUEST,responseId,new Date(),uuids);
    }

    @Override
    public void sendResponse(int priority) {
        long startTime = System.currentTimeMillis() ;
        //根据银行、请求流水号获取已经得到回复的查询（）
        List<CodeConfig> fys = codeConfigDao.getFys();
        for(CodeConfig fy:fys){
            String requester = fy.getCodeKey();
            String wsAddress = fy.getCodeValue();
            List<QueueBank> responses = queueBankDao.getRequestsForSendindResponse(requester, BankService.STATUS_RECEIVE_RESPONSE, priority);
            //提交任务（包括重组、发送）
            if(CollectionUtil.isNotEmpty(responses)){
                Future<List<SendResponseModel>> future = threadPool.submit(new SendResponseModel.SendResponseCallback("send to requester：" + requester, wsAddress, responses));
                try {
                    List<SendResponseModel> models = future.get();
                    for(SendResponseModel model:models){
                        Result result = model.getResult();
                        List<QueueBank> toUpdate = model.getResponses();
                        //判断是否接收成功
                        if(result!=null && result.getValue()!=null && result.getValue()==1){
                            //成功：更新请求
                            queueBankDao.updateForSendResponse(BankService.STATUS_SEND_RESPONSE,new Date(),model.getRequestId());
                        }else{
                            //失败：添加到错误队列
                            String errorMessage = model.getResult() != null ? model.getResult().getMessage() : "未响应成功";
                            bankErrorHandlerScheduleService.addToErrorQueue(toUpdate,errorMessage);
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }

            }
        }
        long endTime = System.currentTimeMillis();
        log.info("sendResponse("+priority+") cost time " + (endTime-startTime) + "ms");
    }

    /**
     * 错误重发成功后恢复请求队列记录的状态
     * @param requestId 请求流水号
     */
    @Override
    @Transactional
    public void recoverSendResponse(String requestId){
        //更新请求队列表
        queueBankDao.updateForSendResponse(BankService.STATUS_SEND_RESPONSE,new Date(),requestId);
        //更新错误队列表
        errorQueueBankDao.updateForResendResponse(BankService.STATUS_SEND_REQUEST,new Date(),requestId);
    }

}
