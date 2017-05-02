package nju.software.sjjh.mock;

import com.alibaba.dubbo.config.annotation.Service;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.bank.model.Result;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 模拟江西高院接收回复
 * Created by Nekonekod on 2017/5/2.
 */
@Slf4j
@Service(protocol = "webservice")
public class JxgyWebServiceImpl implements JxgyWebService {
    @Override
    public String responseAsyncZhye(String params) {
        log.info("JxgyWebService:"+params);
        XStream x = new XStream();
        try {
            Thread.sleep(100); //模拟100ms暂停
            Document document = DocumentHelper.parseText(params);
            Element root = document.getRootElement();
            String rwlsh = root.attribute("FY_QQLSH").getText();
            x.processAnnotations(Result.class);
            return x.toXML(Result.newOkResult(rwlsh));
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return x.toXML(Result.newErrorResult("解析xml错误:"+e.getMessage()));
        }
    }
}
