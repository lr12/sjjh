package nju.software.sjjh.util;

import com.thoughtworks.xstream.XStream;

/**
 * xml转化
 * Created by Nekonekod on 2017/4/17.
 */
public class XmlUtil {

    private static final String XML_HEADER = "<?xml version='1.0' encoding='UTF-8'?>";

    public static String toXml(Object obj){
        XStream x = new XStream();
        x.processAnnotations(obj.getClass());
        return XML_HEADER + x.toXML(obj);
    }

}
