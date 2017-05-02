package nju.software.sjjh.bank.task;

import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.bank.dao.QueueBankDao;
import nju.software.sjjh.bank.entity.QueueBank;
import nju.software.sjjh.bank.service.BankErrorHandlerScheduleService;
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
@Ignore
public class BankTaskTest {

    @Resource
    private BankScheduleService bankScheduleService;

    @Resource
    private BankErrorHandlerScheduleService bankErrorHandlerScheduleService;

    @Resource
    private CodeConfigDao codeConfigDao;

    @Resource
    private QueueBankDao queueBankDao;


    /**
     * 发送请求
     * @throws Exception
     */
    @Test
    public void sendRequest() throws Exception {
        bankScheduleService.sendRequest(BankService.PRIORITY_DEFAULT);
    }

    @Test
    public void sendResponse() throws Exception {
        bankScheduleService.sendResponse(BankService.PRIORITY_DEFAULT);
//        List<QueueBank> list = queueBankDao.getRequestsForSendindResponse("JXFY", BankService.STATUS_RECEIVE_RESPONSE, BankService.PRIORITY_DEFAULT);
//        System.out.println(list);
//        System.out.println(list.size());
    }

    @Test
    public void resendRequest() throws Exception{
        bankErrorHandlerScheduleService.resendRequest(BankService.PRIORITY_DEFAULT);
    }


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
    public void addFackRequests(){
        List<CodeConfig> banks = codeConfigDao.getBanks();
        Random r = new Random();
        String requestId = "QQLSH-"+UuidUtil.generateUuid();
        for(int i=0;i<500;i++){
            QueueBank qb = new QueueBank();
            qb.setUuid("UUID-"+UuidUtil.generateUuid());
            qb.setRequestInterfaceId("requestAsyncZhye");
            qb.setResponseInterfaceId("responseAsyncZhye");
            if(i%5==0){
                requestId = "QQLSH-"+UuidUtil.generateUuid();
            }
            qb.setRequestId(requestId);
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
     * 模拟添加请求数据
     */
    @Test
    public void addFackRequests2(){
        List<CodeConfig> banks = codeConfigDao.getBanks();
        Random r = new Random();
        String requestId = "QQLSH-"+UuidUtil.generateUuid();
        for(int i=0;i<10;i++){
            QueueBank qb = new QueueBank();
            qb.setUuid("UUID-"+UuidUtil.generateUuid());
            qb.setRequestInterfaceId("requestAsyncZhye");
            qb.setResponseInterfaceId("responseAsyncZhye");
            if(i%5==0){
                requestId = "QQLSH-"+UuidUtil.generateUuid();
            }
            qb.setRequestId(requestId);
            String queryId = UuidUtil.generateUuid();
            qb.setQueryId(queryId);
            qb.setRequester("JXFY");
            qb.setStatus(BankService.STATUS_RECEIVE_REQUEST);
            CodeConfig bank = banks.get(0);
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

    @Test
    public void updateFackResponse(){
        List<QueueBank> list = queueBankDao.findByProperty("status", BankService.STATUS_SEND_REQUEST);
        Date now = new Date();
        for(QueueBank qb:list){
            String queryId = qb.getQueryId();
            qb.setStatus(BankService.STATUS_RECEIVE_RESPONSE);
            qb.setReceiveResponseTime(now);
            String result = "";
            if(qb.getDecodedParam().startsWith("<zrr")){
                result = "<zrr\n" +
                        "FY_BS=\""+queryId+"\"\n" +
                        "FY_ZRRXM=\"自然人姓名\"\n" +
                        "FY_ZJLX=\"自然人证件类型\"\n" +
                        "FY_ZJHM=\"自然人证件号码\"\n" +
                        "YH_TXDZ=\"通讯地址\"\n" +
                        "YH_YZBM=\"邮政编码\"\n" +
                        "YH_LXDH=\"联系电话\"\n" +
                        "YH_ZH=\"账号\"\n" +
                        "YH_ZHLB=\"账户类别\"\n" +
                        "YH_ZHZT=\"账户状态\"\n" +
                        "YH_KHH=\"开户行名称\"\n" +
                        "YH_KHHDZ=\"开户行地址\"\n" +
                        "YH_ZCMC=\"资产名称\"\n" +
                        "YH_ZCLX=\"资产类型\"\n" +
                        "YH_ZCDWJG=\"资产单位价格\"\n" +
                        "YH_JLDW=\"计量单位\"\n" +
                        "YH_JJBZ=\"计价币种\"\n" +
                        "YH_ZCSE=\"资产数额\"\n" +
                        "YH_YXQR=\"优先权人\"\n" +
                        "YH_YXQLX=\"优先权类型\"\n" +
                        "YH_YXQJE=\"优先权金额\"\n" +
                        "YH_DJJG=\"冻结机关\"\n" +
                        "YH_DJWH=\"冻结文号\"\n" +
                        "YH_DJJE=\"冻结金额\"\n" +
                        "YH_DJDQSJ=\"冻结到期时间\"\n" +
                        "YH_SJJZSJ=\"数据截至时间\"\n" +
                        "YH_BZ=\"备注\"/>" ;
            }else{
                result = "<zzjg\n" +
                        "FY_BS=\""+queryId+"\"\n" +
                        "FY_ZZJGMC=\"法人或组织机构名称\"\n" +
                        "FY_ZZJGDM=\"组织机构代码\"\n" +
                        "YH_TXDZ=\"通讯地址\"\n" +
                        "YH_YZBM=\"邮政编码\"\n" +
                        "YH_LXDH=\"联系电话\"\n" +
                        "YH_ZH=\"账号\"\n" +
                        "YH_ZHLB=\"账户类别\"\n" +
                        "YH_ZHZT=\"账户状态\"\n" +
                        "YH_KHH=\"开户行名称\"\n" +
                        "YH_KHHDZ=\"开户行地址\"\n" +
                        "YH_ZCMC=\"资产名称\"\n" +
                        "YH_ZCLX=\"资产类型\"\n" +
                        "YH_ZCDWJG=\"资产单位价格\"\n" +
                        "YH_JLDW=\"计量单位\"\n" +
                        "YH_JJBZ=\"计价币种\"\n" +
                        "YH_ZCSE=\"资产数额\"\n" +
                        "YH_YXQR=\"优先权人\"\n" +
                        "YH_YXQLX=\"优先权类型\"\n" +
                        "YH_YXQJE=\"优先权金额\"\n" +
                        "YH_DJJG=\"冻结机关\"\n" +
                        "YH_DJWH=\"冻结文号\"\n" +
                        "YH_DJJE=\"冻结金额\"\n" +
                        "YH_DJDQSJ=\"冻结到期时间\"\n" +
                        "YH_SJJZSJ=\"数据截至时间\"\n" +
                        "YH_BZ=\"备注\"/>";
            }
            qb.setDecodedResult(result);
            queueBankDao.update(qb);
        }
    }


}