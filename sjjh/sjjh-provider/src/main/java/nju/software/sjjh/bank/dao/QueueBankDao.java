package nju.software.sjjh.bank.dao;

import nju.software.sjjh.bank.entity.QueueBank;
import nju.software.sjjh.dao.BaseDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 银行请求队列
 * Created by Nekonekod on 2017/4/17.
 */
@Repository
public class QueueBankDao extends BaseDao<QueueBank> {

    /**
     * 根据回复者，请求状态，优先级获取请求
     * @param status
     * @param priority
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<QueueBank> getRequestByReplierAndStatusAndPriority(String replier,String status, int priority){
        return getHibernateTemplate().find(
                "FROM QueueBank WHERE replier = ? " +
                        "AND status = ? " +
                        "AND priority = ? " +
                        "ORDER BY interfaceId,receiveRequestTime ASC ",
                replier,
                status,
                priority);
    }

    /**
     * 更新发送请求状态
     * @param status 更新后状态
     * @param sendRequestTime 发送请求时间
     * @param queryIds 查询标识
     */
    public void updateForSendRequest(String status, Date sendRequestTime, List queryIds){
        Session session = getSession();
        Query query = session.createQuery("UPDATE QueueBank SET status = :status ,sendRequestTime = :sendRequestTime " +
                "WHERE queryId in (:queryIds)")
                .setParameter("status",status)
                .setParameter("sendRequestTime",sendRequestTime)
                .setParameterList("queryIds", queryIds);
        query.executeUpdate();
    }


    /**
     * 更新错误状态
     * @param status 更新后状态
     * @param queryIds 查询标识
     */
    public void updateForError(String status, List queryIds){
        Session session = getSession();
        Query query = session.createQuery("UPDATE QueueBank SET status = :status " +
                "WHERE queryId in (:queryIds)")
                .setParameter("status",status)
                .setParameterList("queryIds", queryIds);
        query.executeUpdate();
    }

    /**
     * 根据查询id和银行标识获取
     * @param queryId
     * @param responseId
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<QueueBank> getRequestByQueryIdAndResponseId(String queryId,String responseId){
    	return getHibernateTemplate().find(
                "FROM QueueBank WHERE queryId = ? " +
                        "AND responseId = ? ",
                        queryId,
                        responseId);
    }

}
