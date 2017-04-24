package nju.software.sjjh.bank.entity;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 错误队列-银行
 * Created by Nekonekod on 2017/4/17.
 */
@Entity
@Table(name = "ERROR_QUEUE_BANK")
public class ErrorQueueBank {

    /**
     * 主键
     */
    private String uuid;
    /**
     * 接口标识符
     */
    private String interfaceId;
    /**
     * 请求标识
     */
    private String requestId;
    /**
     * 响应标识
     */
    private String responseId;
    /**
     * 查询标识
     */
    private String queryId;
    /**
     * 状态
     */
    private String status;
    /**
     * 发起方
     */
    private String requester;
    /**
     * 回复方
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
    /**
     * 错误次数
     */
    private Integer errorCount;
    /**
     * 错误信息
     */
    private String errorMessage;
    /**
     * 是否忽略
     */
    private String ignore;

    @Id
    @Column(name = "UUID", nullable = false, unique = true)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "INTERFACE_ID")
    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
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

    @Column(name = "ERROR_COUNT")
    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    @Column(name = "ERROR_MESSAGE")
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Column(name = "IGNORE",length = 2)
    public String getIgnore() {
        return ignore;
    }

    public void setIgnore(String ignore) {
        this.ignore = ignore;
    }
}
