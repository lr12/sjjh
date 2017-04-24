package nju.software.sjjh.bank.service;

import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.bank.dao.QueueBankDao;
import nju.software.sjjh.bank.entity.QueueBank;
import nju.software.sjjh.bank.model.Result;
import nju.software.sjjh.dao.CodeConfigDao;
import nju.software.sjjh.entity.CodeConfig;
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

    @Resource
    private CodeConfigDao codeConfigDao;

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
