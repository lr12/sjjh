package nju.software.sjjh.bank.service;

import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.bank.dao.ErrorQueueBankDao;
import nju.software.sjjh.bank.dao.QueueBankDao;
import nju.software.sjjh.bank.entity.ErrorQueueBank;
import nju.software.sjjh.bank.entity.QueueBank;
import nju.software.sjjh.bank.model.Result;
import nju.software.sjjh.bank.model.SendRequestModel;
import nju.software.sjjh.dao.CodeConfigDao;
import nju.software.sjjh.entity.CodeConfig;
import nju.software.sjjh.exception.BaseAppException;
import nju.software.sjjh.util.CollectionUtil;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
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
    private BankErrorHandlerScheduleService bankErrorHandlerScheduleService;

    @Resource
    private CodeConfigDao codeConfigDao;

    @Override
    public void sendRequest(int priority) {
        long startTime = System.currentTimeMillis();
        List<CodeConfig> banks = codeConfigDao.getBanks();
        for (CodeConfig bank:banks) {
            String replierId = bank.getCodeKey();   //协调公司标识
            String wsAddress = bank.getCodeValue(); //ws地址
            //获取所有待发送给银行的请求
            List<QueueBank> requsts = queueBankDao.getRequestByReplierAndStatusAndPriority(
                    replierId, BankService.STATUS_RECEIVE_REQUEST, priority);
            //提交任务
            if(CollectionUtil.isNotEmpty(requsts)){
                Future<List<SendRequestModel>> future = threadPool.submit(new SendRequestModel.SendRequestCallback("send to replier：" + replierId, wsAddress, requsts));
                try {
                    List<SendRequestModel> models = future.get();
                    for(SendRequestModel model:models){
                        Result result = model.getResult();
                        List<QueueBank> toUpdate = model.getRequests();
                        //判断是否接收成功
                        if(result!=null && result.getValue()!=null && result.getValue()==1){
                            //成功：更新请求
                            //获取查询标识集合
                            List<String> queryIds = CollectionUtil.mapping(toUpdate, new CollectionUtil.MappingCallback<QueueBank, String>() {
                                public String map(QueueBank queueBank) {
                                    return queueBank.getQueryId();
                                }
                            });
                            queueBankDao.updateForSendRequest(BankService.STATUS_SEND_REQUEST,model.getResponseId(),new Date(),queryIds);
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

    @Override
    public void sendResponse(int priority) {
        //sned resoinse to court
    }


}
