package nju.software.sjjh.bank.task;

import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.annotation.SjjhTask;
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
public class BankTask {

    @Resource
    private BankService bankService;

    @Scheduled(cron = "0/5 * *  * * ?")
    public void sendRequest() {
        log.info("BankTask : sendRequest");
        bankService.sendRequest();
    }


    @Scheduled(cron = "0/5 * *  * * ?")
    public void sendResponse() {
        log.info("BankTask : sendResponse");
        bankService.sendResponse();
    }


}
