package nju.software.sjjh.util;

import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.common.i18n.Exception;

/**
 * xml转化
 * Created by Nekonekod on 2017/4/17.
 */
@Slf4j
public class XmlUtil {

    private static final String XML_HEADER = "<?xml version='1.0' encoding='UTF-8'?>";

    public static String toXml(Object obj){
        XStream x = new XStream();
        x.processAnnotations(obj.getClass());
        return XML_HEADER + x.toXML(obj);
    }

    @SuppressWarnings("unchecked")
    public static <T> T toBean(String xml,Class<T> clazz){
        XStream x = new XStream();
        x.processAnnotations(clazz);
        return (T) x.fromXML(xml);

    }

}
