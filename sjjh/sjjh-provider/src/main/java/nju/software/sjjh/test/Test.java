package nju.software.sjjh.test;

import nju.software.sjjh.sign.Base64Util;
import nju.software.sjjh.util.CxfUtil;
import nju.software.sjjh.util.WebServiceUtil;

public class Test {

	public static void main(String[] args){
		
		String param="<?xml version='1.0' encoding='UTF-8'?><root><fyid>1700</fyid><bh>7B683851BA5D6108A4AED76869F765D7</bh><ajlb>民事</ajlb></root>";
		param=Base64Util.encode(param);
		try {
			String t=(String)CxfUtil.jump("http://141.1.8.52:8892/nju.software.sjjh.zzfw.IexportdtsServiceZz?wsdl", "exportSPZZCYXX", param)[0];
			//System.out.println(t);
			String ans=Base64Util.decode(t);
			System.out.println(ans);
			//System.out.println(CfxUtil.jump((String)"http://localhost:8892/nju.software.sjjh.webservice.IexportdtsService?wsdl", "getCaseVod", "xml")[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
