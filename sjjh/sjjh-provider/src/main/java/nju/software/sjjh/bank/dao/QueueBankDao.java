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
     * 根据回复者，请求状态，优先级获取待发送给银行的请求
     * @param status
     * @param priority
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<QueueBank> getRequestsForSendingRequest(String replier, String status, int priority){
        return getHibernateTemplate().find(
                "FROM QueueBank WHERE replier = ? " +
                        "AND status = ? " +
                        "AND priority = ? " +
                        "ORDER BY requestInterfaceId,receiveRequestTime ASC ",
                replier,
                status,
                priority);
    }

    /**
     * 获取待通知银行的记录
     * 找到同一个请求流水号里所有记录状态都为RECEIVE_RESPONSE的记录
     */
    @SuppressWarnings("unchecked")
    public List<QueueBank> getRequestsForSendindResponse(String requester,String status,int priority) {
        return getHibernateTemplate().find("FROM QueueBank " +
                "WHERE requester = ? " +
                "AND status = ? " +
                "AND priority = ? " +
                "AND requestId NOT in ( " +
                "  SELECT DISTINCT requestId FROM QueueBank " +
                "  WHERE status <> ? " +
                ") " +
                "ORDER BY requestId,responseInterfaceId ASC ",requester,status,priority,status);
    }

    /**
     * 更新发送请求状态(根据银行标识和查询标识定位、更新)
     * @param status 更新后状态
     * @param responseId 回复流水号
     * @param sendRequestTime 发送请求时间
     * @param uuids 主键
     */
    public void updateForSendRequest(String status,String responseId, Date sendRequestTime,List<String> uuids){
        Session session = getSession();
        Query query = session.createQuery("UPDATE QueueBank SET status = :status ,responseId = :responseId ,sendRequestTime = :sendRequestTime " +
                "WHERE uuid in (:uuids)")
                .setParameter("status",status)
                .setParameter("responseId",responseId)
                .setParameter("sendRequestTime",sendRequestTime)
                .setParameterList("uuids", uuids);
        query.executeUpdate();
    }


    /**
     * 更新发送回复状态（根据请求流水号定位）
     * @param status 更新后状态
     * @param sendResponseTime 发送回复时间
     * @param requestId 请求流水号
     */
    public void updateForSendResponse(String status, Date sendResponseTime,String requestId){
        Session session = getSession();
        Query query = session.createQuery("UPDATE QueueBank SET status = :status ,sendResponseTime = :sendResponseTime " +
                "WHERE requestId = :requestId")
                .setParameter("status",status)
                .setParameter("sendResponseTime",sendResponseTime)
                .setParameter("requestId", requestId);
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
     * @param replier
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<QueueBank> getRequestByQueryIdAndReplier(String queryId, String replier){
    	return getHibernateTemplate().find("FROM QueueBank WHERE queryId = ? AND replier = ? ",queryId,replier);
    }

}
