package nju.software.sjjh.util;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

public class CfxUtil {

	public static Object[] jump(String wsdl,String method,String params) throws Exception{
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();  
		Client client = dcf.createClient(wsdl);  
		  
		Object[] objects = client.invoke(method, params); 
		return objects;
	}
	
}
