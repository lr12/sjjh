package nju.software.sjjh.bank.service;

import nju.software.sjjh.bank.entity.QueueBank;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 错误处理程序服务
 * Created by Nekonekod on 2017/4/26.
 */
public interface BankErrorHandlerScheduleService {

    /**
     * 添加到错误队列，同时修改原请求队列请求状态为错误状态{@link BankService#STATUS_ERROR}
     * @param errorRequests
     * @param errorMessage
     */
    void addToErrorQueue(List<QueueBank> errorRequests, String errorMessage);
}
