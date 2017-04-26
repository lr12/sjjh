package nju.software.sjjh.bank.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.config.PersistenceConfiguration.Strategy;
import nju.software.sjjh.bank.dao.QueueBankDao;
import nju.software.sjjh.bank.entity.QueueBank;
import nju.software.sjjh.bank.model.Result;
import nju.software.sjjh.dao.CodeConfigDao;
import nju.software.sjjh.entity.CodeConfig;
import nju.software.sjjh.strategy.HandleBankParam;
import nju.software.sjjh.strategy.HandleParameter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public Result requestAsyncZhye(String params) {
        //decode params
        log.info(params);
        HandleParameter handleParameter=new HandleBankParam();
        try {
            List<QueueBank> queueBanks =handleParameter.analyticRequestParameter("requestAsyncZhye", params) ;
            for(QueueBank queueBank:queueBanks){
            	queueBankDao.save(queueBank);
            }
            return Result.defaultOK;
        } catch (Exception e) {
            return new Result(0, e.getMessage());
        }
    }

    @Transactional
    @Override
    public Result responseAsyncZhye(String params) {
    	 log.info(params);
         HandleParameter handleParameter=new HandleBankParam();
    	 List<QueueBank> queueBanks =handleParameter.analyticResponseParameter("responseAsyncZhye", params, queueBankDao) ;
    	 try{
    		 queueBankDao.updateList(queueBanks);
    	 }
    	 catch (Exception e) {
			
		}
    	 return null;
    }


}
