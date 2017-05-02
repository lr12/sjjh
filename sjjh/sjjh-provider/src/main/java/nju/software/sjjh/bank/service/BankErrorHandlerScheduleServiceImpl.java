package nju.software.sjjh.bank.service;

import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.bank.dao.ErrorQueueBankDao;
import nju.software.sjjh.bank.dao.QueueBankDao;
import nju.software.sjjh.bank.entity.ErrorQueueBank;
import nju.software.sjjh.bank.entity.QueueBank;
import nju.software.sjjh.bank.model.Result;
import nju.software.sjjh.bank.model.SendRequestModel;
import nju.software.sjjh.bank.model.SendResponseModel;
import nju.software.sjjh.dao.CodeConfigDao;
import nju.software.sjjh.entity.CodeConfig;
import nju.software.sjjh.util.CollectionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.transaction.TransactionManager;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 银行错误处理程序服务实现
 * Created by Nekonekod on 2017/4/26.
 */
@Service
@Slf4j
public class BankErrorHandlerScheduleServiceImpl implements BankErrorHandlerScheduleService{

    /**
     * 线程池
     */
    private ExecutorService threadPool = Executors.newFixedThreadPool(3);

    @Resource
    private CodeConfigDao codeConfigDao;

    @Resource
    private QueueBankDao queueBankDao;

    @Resource
    private ErrorQueueBankDao errorQueueBankDao;

    @Resource
    private BankScheduleService bankScheduleService;


    @Override
    @Transactional
    public void addToErrorQueue(List<QueueBank> errorRequests, String errorMessage){
        //获取查询标识集合
        List<String> queryIds = CollectionUtil.mapping(errorRequests, new CollectionUtil.MappingCallback<QueueBank, String>() {
            public String map(QueueBank queueBank) {
                return queueBank.getQueryId();
            }
        });
        //插入到错误队列表
        for(QueueBank qb:errorRequests){
            boolean exists = true;
            ErrorQueueBank eqb = errorQueueBankDao.get(qb.getUuid());
            if(eqb==null){
                exists = false;
                eqb = new ErrorQueueBank();
            }
            BeanUtils.copyProperties(qb,eqb);
            eqb.setErrorCount(1);
            eqb.setErrorMessage(errorMessage);
            eqb.setDismiss("N");
            if(exists){
                errorQueueBankDao.update(eqb);
            }else{
                errorQueueBankDao.save(eqb);
            }
        }
        //更细请求表状态
        queueBankDao.updateForError(BankService.STATUS_ERROR,queryIds);
    }

    @Override
    public void resendRequest(int priority) {
        long startTime = System.currentTimeMillis();
        List<CodeConfig> banks = codeConfigDao.getBanks();
        for (CodeConfig bank:banks) {
            String replier = bank.getCodeKey();   //协调公司标识
            String wsAddress = bank.getCodeValue(); //ws地址
            //获取所有待发送给银行的请求
            List<ErrorQueueBank> errRequsts = errorQueueBankDao.getRequestsForResendingRequest(
                    replier, BankService.STATUS_RECEIVE_REQUEST, priority,DISMISS_LIMIT);
            List<QueueBank> requests = CollectionUtil.mapping(errRequsts, mappingCallback);
            //提交任务
            if(CollectionUtil.isNotEmpty(requests)){
                Future<List<SendRequestModel>> future = threadPool.submit(new SendRequestModel.SendRequestCallback("send to replier：" + replier, wsAddress, requests));
                try {
                    List<SendRequestModel> models = future.get();
                    for(SendRequestModel model:models){
                        Result result = model.getResult();
                        List<QueueBank> toUpdate = model.getRequests();
                        //获取主键集合
                        List<String> uuids = CollectionUtil.mapping(toUpdate, new CollectionUtil.MappingCallback<QueueBank, String>() {
                            public String map(QueueBank queueBank) {
                                return queueBank.getUuid();
                            }
                        });
                        //判断是否接收成功
                        if(result!=null && result.getValue()!=null && result.getValue()==1){
                            //成功
                            //重置状态
                            bankScheduleService.recoverSendRequest(model.getResponseId(),uuids);
                        }else{
                            //失败
                            String errorMessage = model.getResult() != null ? model.getResult().getMessage() : "未响应成功";
                            errorQueueBankDao.resendFail(uuids,errorMessage);
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("resendRequest("+priority+") cost time " + (endTime-startTime) + "ms");
    }

    @Override
    public void resendResponse(int priority) {
        long startTime = System.currentTimeMillis() ;
        //根据银行、请求流水号获取已经得到回复的查询（）
        List<CodeConfig> fys = codeConfigDao.getFys();
        for(CodeConfig fy:fys){
            String requester = fy.getCodeKey();
            String wsAddress = fy.getCodeValue();
            List<ErrorQueueBank> errResponses = errorQueueBankDao.getRequestsForSendindResponse(requester, BankService.STATUS_RECEIVE_RESPONSE, priority,DISMISS_LIMIT);
            List<QueueBank> responses = CollectionUtil.mapping(errResponses,mappingCallback);
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
                            bankScheduleService.recoverSendResponse(model.getRequestId());
                        }else{
                            //失败：添加到错误队列
                            String errorMessage = model.getResult() != null ? model.getResult().getMessage() : "未响应成功";
                            List<String> uuids = CollectionUtil.mapping(toUpdate, new CollectionUtil.MappingCallback<QueueBank, String>() {
                                @Override
                                public String map(QueueBank queueBank) {
                                    return queueBank.getUuid();
                                }
                            });
                            errorQueueBankDao.resendFail(uuids,errorMessage);
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("resendResponse("+priority+") cost time " + (endTime-startTime) + "ms");
    }

    private CollectionUtil.MappingCallback<ErrorQueueBank, QueueBank> mappingCallback = new CollectionUtil.MappingCallback<ErrorQueueBank, QueueBank>() {
        @Override
        public QueueBank map(ErrorQueueBank eqb) {
            QueueBank qb = new QueueBank();
            BeanUtils.copyProperties(eqb,qb);
            return qb;
        }
    };

}
