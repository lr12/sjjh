package nju.software.sjjh.bank.task;

import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.annotation.SjjhTask;
import nju.software.sjjh.bank.service.BankErrorHandlerScheduleService;
import nju.software.sjjh.bank.service.BankScheduleService;
import nju.software.sjjh.bank.service.BankService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 银行定时任务
 * Created by Nekonekod on 2017/4/18.
 */
@Component
@SjjhTask
@Slf4j
public class BankErrorHandlerTask {

    @Resource
    private BankScheduleService bankScheduleService;
    @Resource
    private BankErrorHandlerScheduleService bankErrorHandlerScheduleService;

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void sendErrorRequestDefault() {
        log.info("BankErrorHandlerTask : resendRequest");
        bankErrorHandlerScheduleService.resendRequest(BankService.PRIORITY_DEFAULT);
    }
    
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void sendErrorResponseDefault() {
        log.info("BankErrorHandlerTask : resendResponse");
        bankErrorHandlerScheduleService.resendResponse(BankService.PRIORITY_DEFAULT);
    }

    @Scheduled(cron = "0 0/15 *  * * ?")
    public void sendErrorRequestHigh() {
        log.info("BankErrorHandlerTask : resendRequest");
        bankErrorHandlerScheduleService.resendRequest(BankService.PRIORITY_HIGH);
    }


    @Scheduled(cron = "0 0/15 *  * * ?")
    public void sendErrorResponseHigh() {
        log.info("BankErrorHandlerTask : resendResponse");
        bankErrorHandlerScheduleService.resendResponse(BankService.PRIORITY_HIGH);
    }

}
