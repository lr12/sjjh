package nju.software.sjjh.bank.dao;

import nju.software.sjjh.bank.entity.QueueBank;
import nju.software.sjjh.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 银行请求队列
 * Created by Nekonekod on 2017/4/17.
 */
@Repository
public class QueueBankDao extends BaseDao<QueueBank> {

    @SuppressWarnings("unchecked")
    public List<QueueBank> getRequestByStatusAndPriority(String status,int priority){
        return getHibernateTemplate().find(
                "FROM QueueBank WHERE status = ? and priority = ? order by replier,receiveRequestTime asc",priority);
    }

}
