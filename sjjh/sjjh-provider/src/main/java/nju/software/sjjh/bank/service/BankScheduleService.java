package nju.software.sjjh.bank.service;

/**
 * 银行定时任务的业务逻辑接口
 * Created by Nekonekod on 2017/4/24.
 */
public interface BankScheduleService {

    /**
     * 发送请求到银行ws
     */
    void sendRequest(int priority);

    /**
     * 通知回复到法院ws
     */
    void sendResponse(int priority);

}
