package nju.software.sjjh.test;

import nju.software.sjjh.util.CxfUtil;
import nju.software.sjjh.util.WebServiceUtil;

public class Test {

	public static void main(String[] args){
		try {
			System.out.println((String)CxfUtil.jump("http://localhost:8892/nju.software.sjjh.webservice.IexportdtsService?wsdl", "sayHello", "xml")[0]);
			//System.out.println(CfxUtil.jump((String)"http://localhost:8892/nju.software.sjjh.webservice.IexportdtsService?wsdl", "getCaseVod", "xml")[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
