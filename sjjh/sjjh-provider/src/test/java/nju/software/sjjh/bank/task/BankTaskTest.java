package nju.software.sjjh.bank.task;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * 测试定时任务
 * Created by Nekonekod on 2017/4/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/*.xml")
@Slf4j
@Ignore
public class BankTaskTest {

    @Resource
    private BankTask task;

    @Test
    public void sendRequest() throws Exception {
        int i= 0;
        while (i<20) {
            System.out.println(++i);
            Thread.sleep(1000);
        }
    }

    @Test
    public void sendResponse() throws Exception {
        task.sendResponse();
    }

}