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
import nju.software.sjjh.util.CollectionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
    private CodeConfigDao codeConfigDao;

    @Override
    public void sendRequest(int priority) {
        List<CodeConfig> banks = codeConfigDao.getBanks();
        for (CodeConfig bank:banks) {
            String replierId = bank.getKey();   //协调公司标识
            String wsAddress = bank.getValue(); //ws地址
            //获取所有待发送给银行的请求
            List<QueueBank> requsts = queueBankDao.getRequestByReplierAndStatusAndPriority(
                    replierId, BankService.STATUS_RECEIVE_REQUEST, priority);
            //提交任务
            Future<List<SendRequestModel>> future = threadPool.submit(new SendRequestModel.SendRequestCallback("send to replier：" + replierId, wsAddress, requsts));
            try {
                List<SendRequestModel> models = future.get();
                for(SendRequestModel model:models){
                    Result result = model.getResult();
                    //获取查询标识集合
                    List<QueueBank> toUpdate = model.getRequests();
                    List<String> queryIds = CollectionUtil.mapping(toUpdate, new CollectionUtil.MappingCallback<QueueBank, String>() {
                        public String map(QueueBank queueBank) {
                            return queueBank.getQueryId();
                        }
                    });
                    //判断是否接收成功
                    if(result!=null && result.getValue()!=null && result.getValue()==1){
                        //成功：更新请求
                        queueBankDao.updateForSendRequest(BankService.STATUS_SEND_REQUEST,new Date(),queryIds);
                    }else{
                        //失败：添加到错误队列并更新请求状态为错误
                        for(QueueBank qb:toUpdate){
                            ErrorQueueBank eqb = new ErrorQueueBank();
                            BeanUtils.copyProperties(qb,eqb);
                            eqb.setErrorCount(1);
                            String errorMessage = model.getResult() != null ? model.getResult().getMessage() : "未响应成功";
                            eqb.setErrorMessage(errorMessage);
                            eqb.setIgnore("N");
                            errorQueueBankDao.save(eqb);
                        }
                        queueBankDao.updateForError(BankService.STATUS_ERROR,queryIds);
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage(),e);
            }
        }
    }

    @Override
    public void sendResponse(int priority) {
        //sned resoinse to court
    }


}
