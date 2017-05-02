package nju.software.sjjh.bank.dao;

import nju.software.sjjh.bank.entity.ErrorQueueBank;
import nju.software.sjjh.bank.entity.QueueBank;
import nju.software.sjjh.dao.BaseDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 错误队列数据访问对象
 * Created by Nekonekod on 2017/4/24.
 */
@Repository
public class ErrorQueueBankDao extends BaseDao<ErrorQueueBank> {

    /**
     * 根据回复者，请求状态，优先级获取待发送给银行的请求
     * @param replier 回复者
     * @param status 状态
     * @param priority 优先级
     * @param errorCountLimit >=错误次数则忽略
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ErrorQueueBank> getRequestsForResendingRequest(String replier, String status, int priority,int dismissLimit){
        return getHibernateTemplate().find(
                "FROM ErrorQueueBank WHERE replier = ? " +
                        "AND status = ? " +
                        "AND priority = ? " +
                        "AND errorCount <= ? " +
                        "AND dismiss = 'N' " +
                        "ORDER BY requestInterfaceId,receiveRequestTime ASC ",
                replier,
                status,
                priority,
                dismissLimit);
    }

    /**
     * 获取待通知银行的记录
     * 找到同一个请求流水号里所有记录状态都为RECEIVE_RESPONSE的记录
     */
    @SuppressWarnings("unchecked")
    public List<ErrorQueueBank> getRequestsForSendindResponse(String requester,String status,int priority,int dismissLimit) {
        return getHibernateTemplate().find("FROM ErrorQueueBank " +
                "WHERE requester = ? " +
                "AND status = ? " +
                "AND priority = ? " +
                "AND errorCount <= ? " +
                "AND requestId NOT in ( " +
                "  SELECT DISTINCT requestId FROM QueueBank " +
                "  WHERE status <> ? " +
                ") " +
                "ORDER BY requestId,responseInterfaceId ASC ",requester,status,priority,dismissLimit,status);
    }

    /**
     * 重发请求成功后更新
     * @param status
     * @param responseId
     * @param sendRequestTime
     * @param uuids
     */
    public void updateForResendRequest(String status, String responseId, Date sendRequestTime, List<String> uuids) {
        Session session = getSession();
        Query query = session.createQuery("UPDATE ErrorQueueBank SET status = :status ,responseId = :responseId ,sendRequestTime = :sendRequestTime ,dismiss = :dismiss " +
                "WHERE uuid in (:uuids)")
                .setParameter("status",status)
                .setParameter("responseId",responseId)
                .setParameter("sendRequestTime",sendRequestTime)
                .setParameter("dismiss","Y")
                .setParameterList("uuids", uuids);
        query.executeUpdate();
    }


    /**
     * 更新发送回复状态（根据请求流水号定位）
     * @param status 更新后状态
     * @param sendResponseTime 发送回复时间
     * @param requestId 请求流水号
     */
    public void updateForResendResponse(String status, Date sendResponseTime,String requestId){
        Session session = getSession();
        Query query = session.createQuery("UPDATE ErrorQueueBank SET status = :status ,sendResponseTime = :sendResponseTime,dismiss = :dismiss " +
                "WHERE requestId = :requestId")
                .setParameter("status",status)
                .setParameter("sendResponseTime",sendResponseTime)
                .setParameter("dismiss","Y")
                .setParameter("requestId", requestId);
        query.executeUpdate();
    }

    /**
     * 重发失败:错误次数+1，更新错误消息
     * @param uuids
     * @param errorMessage
     */
    public void resendFail(List<String> uuids,String errorMessage){
        Session session = getSession();
        Query query = session.createQuery("UPDATE ErrorQueueBank SET errorCount = errorCount + 1,errorMessage = :errorMessage " +
                "WHERE uuid in (:uuids)")
                .setParameter("errorMessage",errorMessage)
                .setParameterList("uuids", uuids);
        query.executeUpdate();
    }


}
