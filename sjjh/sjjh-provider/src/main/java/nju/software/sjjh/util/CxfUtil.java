package nju.software.sjjh.util;


import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class CxfUtil {


	private static Map<String,Client> cache = new HashMap<>();

	public static Object[] jump(String wsdl,String method,String params) throws Exception{
		log.info("wsdl:"+wsdl+";method:"+method+";params:"+params);
		Client client;
		if(cache.containsKey(wsdl)){
			client = cache.get(wsdl);
		}else{
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			client = dcf.createClient(wsdl);
			cache.put(wsdl,client);
		}
		
		Object[] objects = client.invoke(method, params);
		return objects;
	}
	
}
