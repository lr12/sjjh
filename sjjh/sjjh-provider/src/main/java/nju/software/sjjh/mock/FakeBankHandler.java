package nju.software.sjjh.mock;

import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import nju.software.sjjh.bank.model.Result;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Created by Nekonekod on 2017/4/27.
 */
@Slf4j
public class FakeBankHandler {

    public static String responseAsyncZhye(String tag,String params){
        log.info(params);
        XStream x = new XStream();
        try {
            Thread.sleep(100); //模拟100ms暂停
            Document document = DocumentHelper.parseText(params);
            Element root = document.getRootElement();
            String rwlsh = root.attribute("FY_RWLSH").getText();
            x.processAnnotations(Result.class);
            return x.toXML(Result.newOkResult(rwlsh));
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return x.toXML(Result.newErrorResult("解析xml错误:"+e.getMessage()));
        }
    }
}
