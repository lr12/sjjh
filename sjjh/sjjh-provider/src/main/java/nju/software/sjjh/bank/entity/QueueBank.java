package nju.software.sjjh.bank.entity;


import java.util.Date;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 请求队列-银行
 * Created by Nekonekod on 2017/4/17.
 */
@Entity
@Table(name = "QUEUE_BANK")
public class QueueBank {

    /**
     * 主键
     */
    private String uuid;
    /**
     * 请求流水号
     */
    private String requestId;
    /**
     * 响应流水号
     */
    private String responseId;
    /**
     * 请求方接口标识符（通知结果时调用，业务系统提供）
     */
    private String requestInterfaceId;
    /**
     * 回复方接口标识符（发送请求时调用，协同系统提供）
     */
    private String responseInterfaceId;
    /**
     * 查询标识
     */
    private String queryId;
    /**
     * 状态
     */
    private String status;
    /**
     * 发起方--法院标识
     */
    private String requester;
    /**
     * 回复方--银行标识
     */
    private String replier;
    /**
     * 接收请求时间
     */
    private Date receiveRequestTime;
    /**
     * 调用协调系统时间
     */
    private Date sendRequestTime;
    /**
     * 接收回复时间
     */
    private Date receiveResponseTime;
    /**
     * 通知回复时间
     */
    private Date sendResponseTime;
    /**
     * 解析后的查询参数
     */
    private String decodedParam;
    /**
     * 解析后的查询结果
     */
    private String decodedResult;
    /**
     * 优先级
     */
    private Integer priority;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "UUID", nullable = false, unique = true)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    @Column(name = "REQUEST_INTERFACE_ID")
    public String getRequestInterfaceId() {
        return requestInterfaceId;
    }

    public void setRequestInterfaceId(String requestInterfaceId) {
        this.requestInterfaceId = requestInterfaceId;
    }

    @Column(name = "RESPONSE_INTERFACE_ID")
    public String getResponseInterfaceId() {
        return responseInterfaceId;
    }

    public void setResponseInterfaceId(String responseInterfaceId) {
        this.responseInterfaceId = responseInterfaceId;
    }

    @Column(name = "QUERY_ID",unique = true)
    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "REQUESTER")
    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    @Column(name = "REPLIER")
    public String getReplier() {
        return replier;
    }

    public void setReplier(String replier) {
        this.replier = replier;
    }


    @Column(name = "DECODED_PARAM")
    public String getDecodedParam() {
        return decodedParam;
    }

    public void setDecodedParam(String decodedParam) {
        this.decodedParam = decodedParam;
    }

    @Column(name = "PRIORITY")
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Column(name = "REQUEST_ID")
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Column(name = "RESPONSE_ID")
    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    @Column(name = "RECEIVE_REQUEST_TIME")
    public Date getReceiveRequestTime() {
        return receiveRequestTime;
    }

    public void setReceiveRequestTime(Date receiveRequestTime) {
        this.receiveRequestTime = receiveRequestTime;
    }

    @Column(name = "SEND_REQUEST_TIME")
    public Date getSendRequestTime() {
        return sendRequestTime;
    }

    public void setSendRequestTime(Date sendRequestTime) {
        this.sendRequestTime = sendRequestTime;
    }

    @Column(name = "RECEIVE_RESPONSE_TIME")
    public Date getReceiveResponseTime() {
        return receiveResponseTime;
    }

    public void setReceiveResponseTime(Date receiveResponseTime) {
        this.receiveResponseTime = receiveResponseTime;
    }

    @Column(name = "SEND_RESPONSE_TIME")
    public Date getSendResponseTime() {
        return sendResponseTime;
    }

    public void setSendResponseTime(Date sendResponseTime) {
        this.sendResponseTime = sendResponseTime;
    }

    @Column(name = "DECODED_RESULT")
    public String getDecodedResult() {
        return decodedResult;
    }

    public void setDecodedResult(String decodedResult) {
        this.decodedResult = decodedResult;
    }


	public QueueBank() {
		super();
	}

    public QueueBank(String uuid, String requestId, String responseId, String requestInterfaceId, String responseInterfaceId, String queryId, String status, String requester, String replier, Date receiveRequestTime, Date sendRequestTime, Date receiveResponseTime, Date sendResponseTime, String decodedParam, String decodedResult, Integer priority) {
        this.uuid = uuid;
        this.requestId = requestId;
        this.responseId = responseId;
        this.requestInterfaceId = requestInterfaceId;
        this.responseInterfaceId = responseInterfaceId;
        this.queryId = queryId;
        this.status = status;
        this.requester = requester;
        this.replier = replier;
        this.receiveRequestTime = receiveRequestTime;
        this.sendRequestTime = sendRequestTime;
        this.receiveResponseTime = receiveResponseTime;
        this.sendResponseTime = sendResponseTime;
        this.decodedParam = decodedParam;
        this.decodedResult = decodedResult;
        this.priority = priority;
    }
}
