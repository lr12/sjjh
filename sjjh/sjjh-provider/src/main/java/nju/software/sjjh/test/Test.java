package nju.software.sjjh.test;

import nju.software.sjjh.util.CfxUtil;
import nju.software.sjjh.util.WebServiceUtil;

public class Test {

	public static void main(String[] args){
		try {
			System.out.println(WebServiceUtil.invode("http://localhost:8892/nju.software.sjjh.webservice.IexportdtsService?wsdl", "sayHello", "xml", "xml"));
			//System.out.println(CfxUtil.jump((String)"http://localhost:8892/nju.software.sjjh.webservice.IexportdtsService?wsdl", "getCaseVod", "xml")[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
