package nju.software.sjjh.bank.facade;

import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.bank.model.Result;
import nju.software.sjjh.bank.service.BankService;
import nju.software.sjjh.util.XmlUtil;

import javax.annotation.Resource;

/**
 * 银行接口实现
 * Created by Nekonekod on 2017/4/17.
 */
@Service(protocol = {"webservice"})
@Slf4j
public class CourtToBankWebServiceImpl implements CourtToBankWebService{

    @Resource
    private BankService bankService;

    @Override
    public String responseAsyncZhye(String params) {
        Result result = bankService.responseAsyncZhye(params);
        return XmlUtil.toXml(result);
    }
}