package nju.software.sjjh.main;

import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.bank.dao.QueueBankDao;
import nju.software.sjjh.bank.facade.BankWebServiceImpl;
import nju.software.sjjh.bank.service.BankServiceImpl;
import nju.software.sjjh.bank.task.BankTask;
import nju.software.sjjh.service.HelloService;
import nju.software.sjjh.util.CfxUtil;
import nju.software.sjjh.util.WebServiceUtil;
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
@ContextConfiguration("classpath:spring/spring-config.xml")
@Slf4j
public class MainTest {

    @Resource
    private HelloService helloService;

    @Test
    @Ignore
    public void main() throws Exception {
        ApplicationContext context = Main.getContext();
        log.info(""+context.getBean(BankWebServiceImpl.class));
        log.info(""+context.getBean(BankServiceImpl.class));
        log.info(""+context.getBean(QueueBankDao.class));
        log.info(""+context.getBean(BankTask.class));
        log.info(""+context.getBean(HelloService.class));

    }

    @Test
    @Ignore
    public void testCache(){
        log.info(helloService.getStr("第一"));
        log.info(helloService.getStr("第一"));
    }

    @Test
    @Ignore
    public void testWsUtil(){
        long t1 = System.currentTimeMillis();
        String result = WebServiceUtil.invode(
                "http://192.168.0.103:8892/nju.software.sjjh.mock.GsyhWebService?wsdl",
                "responseAsyncZhye",
                "arg0",
                "hhhhh");
        long t2 = System.currentTimeMillis();
        System.out.println(result+":"+(t2-t1));
        try {
            Object[] r = CfxUtil.jump("http://192.168.0.103:8892/nju.software.sjjh.mock.GsyhWebService?wsdl", "responseAsyncZhye", "hhhhh");
            long t3 = System.currentTimeMillis();
            System.out.println(r[0]+":"+(t3-t2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}