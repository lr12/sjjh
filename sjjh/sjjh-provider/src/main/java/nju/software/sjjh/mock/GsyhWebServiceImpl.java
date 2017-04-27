package nju.software.sjjh.mock;

import com.alibaba.dubbo.config.annotation.Service;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.bank.model.Result;

/**
 * Created by Nekonekod on 2017/4/26.
 */
@Slf4j
@Service(protocol = {"webservice"})
public class GsyhWebServiceImpl implements GsyhWebService {

    @Override
    public String responseAsyncZhye(String params) {
        XStream x = new XStream();
        x.processAnnotations(Result.class);
        return x.toXML(Result.newErrorResult("测试"));
//        return FakeBankHandler.responseAsyncZhye(this.getClass().getName(),params);
    }
}
