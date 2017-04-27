package nju.software.sjjh.bank.task;

import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.bank.dao.QueueBankDao;
import nju.software.sjjh.bank.entity.QueueBank;
import nju.software.sjjh.bank.service.BankScheduleService;
import nju.software.sjjh.bank.service.BankService;
import nju.software.sjjh.dao.CodeConfigDao;
import nju.software.sjjh.entity.CodeConfig;
import nju.software.sjjh.util.UuidUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Request;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 测试定时任务
 * Created by Nekonekod on 2017/4/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-config.xml")
@Slf4j
public class BankTaskTest {

    @Resource
    private BankScheduleService bankScheduleService;

    @Resource
    private CodeConfigDao codeConfigDao;

    @Resource
    private QueueBankDao queueBankDao;

    /**
     * 模拟银行和银行地址映射
     */
    @Test
    @Ignore
    public void addBankCode(){
        CodeConfig c = new CodeConfig(1,"银行标识","GSYH",
                "http://127.0.0.1:8892/nju.software.sjjh.mock.GsyhWebService?wsdl","工商银行",null);
        codeConfigDao.save(c);

        CodeConfig c2 = new CodeConfig(2,"银行标识","NYYH",
                "http://127.0.0.1:8892/nju.software.sjjh.mock.NyyhWebService?wsdl","农业银行",null);
        codeConfigDao.save(c2);

        CodeConfig c3 = new CodeConfig(3,"银行标识","ZSYH",
                "http://127.0.0.1:8892/nju.software.sjjh.mock.ZsyhWebService?wsdl","招商银行",null);
        codeConfigDao.save(c3);
    }

    /**
     * 模拟添加请求数据
     */
    @Test
    @Ignore
    public void addFackRequests(){
        List<CodeConfig> banks = codeConfigDao.getBanks();
        Random r = new Random();
        for(int i=0;i<500;i++){
            QueueBank qb = new QueueBank();
            qb.setUuid("UUID-"+UuidUtil.generateUuid());
            qb.setRequestInterfaceId("requestAsyncZhye");
            qb.setRequestId("QQLSH-"+UuidUtil.generateUuid());
            qb.setResponseId("WLSLH-"+UuidUtil.generateUuid());
            String queryId = UuidUtil.generateUuid();
            qb.setQueryId(queryId);
            qb.setRequester("JXFY");
            qb.setStatus(BankService.STATUS_RECEIVE_REQUEST);
            CodeConfig bank = banks.get(r.nextInt(banks.size()));
            qb.setReplier(bank.getCodeKey());
            qb.setReceiveRequestTime(new Date());
            String params = "";
            if(r.nextInt(100)%2==0){
                params = "<zrr\n" +
                        "FY_BS=\""+queryId+"\"\n" +
                        "FY_ZJLX=\"自然人证件类型\"\n" +
                        "FY_ZJHM=\"自然人证件号码\"\n" +
                        "FY_ZXFYMC=\"执行法院名称\"\n" +
                        "FY_CBR=\"承办人\"\n" +
                        "FY_ZXAH=\"执行案号\"\n" +
                        "FY_FLWSDZ=\"法律文书地址\"/>";
            }else{
                params = "<zzjg\n" +
                        "FY_BS=\""+queryId+"\"\n" +
                        "FY_ZZJGMC=\"组织机构"+i+"\"\n" +
                        "FY_ZZJGDM=\"组织机构代码\"\n" +
                        "FY_ZXFYMC=\"执行法院名称\"\n" +
                        "FY_CBR=\"承办人\"\n" +
                        "FY_ZXAH=\"执行案号\"\n" +
                        "FY_FLWSDZ=\"法律文书地址\"/>";
            }
            qb.setDecodedParam(params);
            qb.setPriority(BankService.PRIORITY_DEFAULT);
            queueBankDao.save(qb);
        }

    }

    /**
     * 发送请求
     * @throws Exception
     */
    @Test
    public void sendRequest() throws Exception {
        bankScheduleService.sendRequest(BankService.PRIORITY_DEFAULT);
    }

    @Test
    @Ignore
    public void sendResponse() throws Exception {
//        bankScheduleService.sendResponse();
    }

}