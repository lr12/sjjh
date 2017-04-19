package nju.software.sjjh.main;

import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.bank.dao.QueueBankDao;
import nju.software.sjjh.bank.facade.BankWebServiceImpl;
import nju.software.sjjh.bank.service.BankServiceImpl;
import nju.software.sjjh.bank.task.BankTask;
import nju.software.sjjh.service.HelloService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by Nekonekod on 2017/4/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/*.xml")
@Slf4j
@Ignore
public class MainTest {

    @Resource
    private HelloService helloService;

    @Test
    public void main() throws Exception {
        ApplicationContext context = Main.getContext();
        log.info(""+context.getBean(BankWebServiceImpl.class));
        log.info(""+context.getBean(BankServiceImpl.class));
        log.info(""+context.getBean(QueueBankDao.class));
        log.info(""+context.getBean(BankTask.class));
        log.info(""+context.getBean(HelloService.class));

    }

    @Test
    public void testCache(){
        log.info(helloService.getStr("第一"));
        log.info(helloService.getStr("第一"));
    }

}