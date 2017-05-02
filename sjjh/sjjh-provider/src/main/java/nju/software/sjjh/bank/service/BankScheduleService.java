package nju.software.sjjh.bank.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 银行定时任务的业务逻辑接口
 * Created by Nekonekod on 2017/4/24.
 */
public interface BankScheduleService {

    /**
     * 发送请求到银行ws
     */
    void sendRequest(int priority);

    @Transactional
    void recoverSendRequest(String responseId, List<String> uuids);

    /**
     * 通知回复到法院ws
     */
    void sendResponse(int priority);

    @Transactional
    void recoverSendResponse(String requestId);
}
