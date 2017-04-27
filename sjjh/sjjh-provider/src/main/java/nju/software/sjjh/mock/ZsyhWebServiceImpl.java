package nju.software.sjjh.mock;

import com.alibaba.dubbo.config.annotation.Service;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.bank.model.Result;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Created by Nekonekod on 2017/4/26.
 */
@Slf4j
@Service(protocol = {"webservice"})
public class ZsyhWebServiceImpl implements ZsyhWebService {

    @Override
    public String responseAsyncZhye(String params) {
        return FakeBankHandler.responseAsyncZhye(this.getClass().getName(),params);
    }
}
