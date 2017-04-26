package nju.software.sjjh.bank.service;

import nju.software.sjjh.bank.model.Result;

/**
 * 处理银行数据交换业务
 * Created by Nekonekod on 2017/4/17.
 */
public interface BankService {

    /**
     * 状态 接收请求
     */
    String STATUS_RECEIVE_REQUEST = "RECEIVE_REQUEST";
    /**
     * 状态 发送请求
     */
    String STATUS_SEND_REQUEST = "SEND_REQUEST";
    /**
     * 状态 接收回复
     */
    String STATUS_RECEIVE_RESPONSE = "RECEIVE_RESPONSE";
    /**
     * 状态 发送回复
     */
    String STATUS_SEND_RESPONSE = "SEND_RESPONSE";
    /**
     * 状态 错误
     */
    String STATUS_ERROR = "ERROR";

    /**
     * 高优先级
     */
    int PRIORITY_HIGH = 10;
    /**
     * 默认优先级
     */
    int PRIORITY_DEFAULT = 0;

    /**
     * 添加查询账户余额请求
     * @param params
     * @return
     */
    Result requestAsyncZhye(String params);

    /**
     * 接收查询账户余额恢复
     * @param params
     * @return
     */
    Result responseAsyncZhye(String params);

}
