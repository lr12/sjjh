package nju.software.sjjh.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FilenameUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.InputSource;

import nju.software.sjjh.sign.Base64Util;
import nju.software.sjjh.util.CxfUtil;
import nju.software.sjjh.util.Dom4jUtil;
import nju.software.sjjh.util.WebServiceUtil;

@Slf4j
public class Test {

	static Map<String, String> map=new HashMap<String, String>();
	
	public static void main(String[] args){
		map.put("exportFTXX", "<?xml version='1.0' encoding='UTF-8'?><root><fyid>1701</fyid></root>");
		map.put("exportRYXX", "<?xml version='1.0' encoding='UTF-8'?><root><fyid>1701</fyid></root>");
		map.put("exportKTXX", "<?xml version='1.0' encoding='UTF-8'?><root><fyid>1701</fyid><ftxh>111476742</ftxh> <begin>2016-02-01</begin><end>2017-05-19</end></root>");
		map.put("exportAJJBXX", "<?xml version='1.0' encoding='UTF-8'?><root><fyid>1701</fyid><bh>4D515C25910FCB40D3D2AB3069B962A1</bh><ajlb>刑事</ajlb></root>");
		map.put("exportDSRXX", "<?xml version='1.0' encoding='UTF-8'?><root><fyid>1701</fyid><bh>4D515C25910FCB40D3D2AB3069B962A1</bh><ajlb>刑事</ajlb></root>");
		map.put("exportSPZZCYXX", "<?xml version='1.0' encoding='UTF-8'?><root><fyid>1701</fyid><bh>4D515C25910FCB40D3D2AB3069B962A1</bh><ajlb>刑事</ajlb></root>");
		String ktxx="<?xml version='1.0' encoding='UTF-8'?><root><fyid>1701</fyid><ftxh>111476742</ftxh> <begin>2015-05-19</begin><end>2017-05-19</end></root>";
		String ajjbxx="<?xml version='1.0' encoding='UTF-8'?><root><fyid>1701</fyid><bh>4D515C25910FCB40D3D2AB3069B962A1</bh><ajlb>刑事</ajlb></root>";
		
		String interfaceIdString="exportFTXX";
		String param=map.get(interfaceIdString);
		param=Base64Util.encode(param);
		try {
			String t=(String)CxfUtil.jump("http://141.1.8.52:8892/nju.software.sjjh.zzfw.IexportdtsServiceZz?wsdl", interfaceIdString, param)[0];
			//System.out.println(t);
			String ans=Base64Util.decode(t);
			Element element=Dom4jUtil.getRooElement(ans);
			Iterator<Element> ftIterator = Dom4jUtil
					.getCurrentNameIeraIterator(element, "ft");
			int count=0;
			while(ftIterator.hasNext()){
				Element ftElement=ftIterator.next();
				Map<String, String> map=Dom4jUtil.getAttributeMap(ftElement);
				//System.out.println(map.get("ftxh"));
				String temp=ktxx.replaceAll("111476742", map.get("ftxh"));
				param=Base64Util.encode(temp);
				//获取法庭的案件信息
				interfaceIdString="exportKTXX";
				String ftxx=(String)CxfUtil.jump("http://141.1.8.52:8892/nju.software.sjjh.zzfw.IexportdtsServiceZz?wsdl", interfaceIdString, param)[0];
				//System.out.println(ftxx);
				/*write(interfaceIdString, "南昌中  "+(count++)+"", ftxx);*/
				ftxx=Base64Util.decode(ftxx);
			
				Element kt=Dom4jUtil.getRooElement(ftxx);
				Iterator<Element> ktIterator=Dom4jUtil.getCurrentNameIeraIterator(kt, "ttrialplan");
				while(ktIterator.hasNext()){
					Element ktElement=ktIterator.next();
					//System.out.println(ktElement.asXML());
					try{
						String caseId=ktElement.element("caseid").getTextTrim();
						String ajlb=ktElement.element("ajlb").getTextTrim();
					    //生成案件信息
						temp=ajjbxx.replaceAll("4D515C25910FCB40D3D2AB3069B962A1", caseId);
					    param=temp.replaceAll("刑事", ajlb);
					    param=Base64Util.encode(param);
					    interfaceIdString="exportAJJBXX";
						String result=(String)CxfUtil.jump("http://141.1.8.52:8892/nju.software.sjjh.zzfw.IexportdtsServiceZz?wsdl", interfaceIdString, param)[0];
					
						write(interfaceIdString, caseId, result);
					    interfaceIdString="exportDSRXX";
						result=(String)CxfUtil.jump("http://141.1.8.52:8892/nju.software.sjjh.zzfw.IexportdtsServiceZz?wsdl", interfaceIdString, param)[0];
					
						write(interfaceIdString, caseId, result);
					    interfaceIdString="exportSPZZCYXX";
					 
					    result=(String)CxfUtil.jump("http://141.1.8.52:8892/nju.software.sjjh.zzfw.IexportdtsServiceZz?wsdl", interfaceIdString, param)[0];
					    write(interfaceIdString, caseId, result);
					   
					}
					catch (Exception e) {
						e.printStackTrace();
						log.error("",e);
					}
					
				}
			}
			//System.out.println(ans);
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error("",e);
		}
	}
	
	
	public static void write(String interfaceIdString,String caseId,String xml){
		String path="C:\\Users\\lr12\\Desktop\\dom4jForJavaXML\\dom4j\\temp\\"+caseId;
		//path="./1701/"+caseId;
		String ans=Base64Util.decode(xml);
		log.info(ans);
		try {
			File file=new File(path);
			if(!file.exists()){
				file.mkdir();
			}
			//System.out.println(ans);
			StringReader stringReader = new StringReader(ans);
			InputSource source = new InputSource(stringReader);
			// 创建SAXReader对象
			SAXReader reader = new SAXReader();
			// 读取文件 转换成Document
			Document document = reader.read(source);
			
			OutputFormat format = OutputFormat.createPrettyPrint();
			//设置编码
			format.setEncoding("UTF-8");
			//XMLWriter 指定输出文件以及格式
			XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(new File(path+"//"+interfaceIdString+".xml")),"UTF-8"), format);
			//写入新文件
			writer.write(document);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			log.error(ans,e);
			e.printStackTrace();
		}
	}
}
