package nju.software.sjjh.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.log4j.Logger;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import java.util.Arrays;

/**
 * 发送到WebService
 * Created by Nekonekod on 2017/4/24.
 */
@Slf4j
public class WebServiceUtil {

    public static String invode(String address, String funName, String paramName, String paramValue) {
        return invoke(address, "", funName, new String[]{paramName}, new String[]{paramValue});
    }

    public static String invode(String address, String soapAction, String funName, String paramName, String paramValue) {
        return invoke(address, soapAction, funName, new String[]{paramName}, new String[]{paramValue});
    }

    public static String invode(String address, String funName, String[] paramNames, String[] paramValues) {
        return invoke(address, "", funName, paramNames, paramValues);
    }

    private static String invoke(String address, String soapAction, String funName, String[] paramNames, String[] paramValues) {
        log.warn("监狱webservice：" + funName);
        try {
            Service s = new Service();
            Call call = (Call) s.createCall();
            call.setSOAPActionURI(soapAction + funName);
            call.setOperationName(new QName(soapAction, funName));
            call.setTargetEndpointAddress(address);
            for (int i = 0; i < paramNames.length; i++) {
                String name = paramNames[i];
                call.addParameter(new QName(soapAction, name), XMLType.XSD_STRING, ParameterMode.IN);
            }
            return (String) call.invoke(paramValues);
        } catch (Exception e) {
            log.error("调用webservice失败:" + Arrays.toString(new String[]{
                    "address:"+address,
                    "soapAction:"+soapAction,
                    "funName:"+funName,
                    "paramNames:"+Arrays.toString(paramNames),
                    "paramValues:"+Arrays.toString(paramValues),
            }), e);
        }
        return null;
    }

}