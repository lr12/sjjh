package nju.software.sjjh.bank.service;

import nju.software.sjjh.bank.dao.ErrorQueueBankDao;
import nju.software.sjjh.bank.dao.QueueBankDao;
import nju.software.sjjh.bank.entity.ErrorQueueBank;
import nju.software.sjjh.bank.entity.QueueBank;
import nju.software.sjjh.util.CollectionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 银行错误处理程序服务实现
 * Created by Nekonekod on 2017/4/26.
 */
@Service
public class BankErrorHandlerScheduleServiceImpl implements BankErrorHandlerScheduleService{

    @Resource
    private QueueBankDao queueBankDao;

    @Resource
    private ErrorQueueBankDao errorQueueBankDao;


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
            ErrorQueueBank eqb = new ErrorQueueBank();
            BeanUtils.copyProperties(qb,eqb);
            eqb.setErrorCount(1);
            eqb.setErrorMessage(errorMessage);
            eqb.setIgnore("N");
            errorQueueBankDao.save(eqb);
        }
        //更细请求表状态
        queueBankDao.updateForError(BankService.STATUS_ERROR,queryIds);
    }

}
