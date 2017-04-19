package nju.software.sjjh.bank.service;

import nju.software.sjjh.bank.model.Result;

/**
 * 处理银行数据交换业务
 * Created by Nekonekod on 2017/4/17.
 */
public interface BankService {


    void sendRequest();

    void sendResponse();

    /**
     * 添加查询账户余额请求
     * @param params
     * @return
     */
    Result requestAsyncZhye(String[] params);

    /**
     * 接收查询账户余额恢复
     * @param params
     * @return
     */
    Result responseAsyncZhye(String params);

}
