package nju.software.sjjh.bank.service;

import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.bank.dao.QueueBankDao;
import nju.software.sjjh.bank.entity.QueueBank;
import nju.software.sjjh.bank.model.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 银行数据交换业务实现类
 * Created by Nekonekod on 2017/4/17.
 */
@Service
@Slf4j
public class BankServiceImpl implements BankService {

    @Resource
    private QueueBankDao queueBankDao;


    @Override
    public void sendRequest(int priority) {
        //send request to bank
        //1.获取所有状态为接收请求
        List<QueueBank> list = queueBankDao.getRequestByStatusAndPriority(STATUS_RECEIVE_REQUEST, priority);
        //2.TODO 拆分到各个银行
//        List<SendRequestBankModel> models = SendRequestBankModel.buildModelsFromList(list);
//        for(final SendRequestBankModel model:models){
//            //每个银行一个线程
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    String bankId = model.getBankId();
//                    log.info("sendRequest bankId : "+bankId);
//                    Map<String, List<QueueBank>> requestMap = model.getMap();
//                    //TODO get ws address by bankId
//                    String wsAddress = null;
//                    //划分每个接口
//                    for(Map.Entry<String,List<QueueBank>> entry:requestMap.entrySet()){
//                        String interfaceId = entry.getKey();
//                        List<QueueBank> requests = entry.getValue();
//                        //重组请求
//                        List<SendRequestModel> srms = SendRequestModel.rebuildRequest(requests,10);
//                        //发送请求
//                        for(SendRequestModel srm:srms){
//                            //TODO 发送到webService
//
//                        }
//                    }
//                }
//            }).start();
//        }
    }

    @Override
    public void sendResponse(int priority) {
        //sned resoinse to court
    }

    @Override
    public Result requestAsyncZhye(String[] params) {
        //decode params
        log.info("length:" + params.length + Arrays.toString(params));
        try {
            QueueBank queueBank = new QueueBank();
            queueBank.setInterfaceId("int1");
           
            queueBank.setQueryId("query1");
            queueBankDao.save(queueBank);
            return Result.defaultOK;
        } catch (Exception e) {
            return new Result(0, e.getMessage());
        }
    }

    @Override
    public Result responseAsyncZhye(String params) {
        //get id
        //find queuebank
        //update
        return null;
    }


}
