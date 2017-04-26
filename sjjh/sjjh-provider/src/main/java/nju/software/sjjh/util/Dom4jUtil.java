package nju.software.sjjh.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.xml.sax.InputSource;

public class Dom4jUtil {

	/**
	 * 根据字符串获取根节点
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	public static Element getRooElement(String xml) throws DocumentException {
		StringReader stringReader = new StringReader(xml);
		InputSource source = new InputSource(stringReader);
		// 创建SAXReader对象
		SAXReader reader = new SAXReader();
		// 读取文件 转换成Document
		Document document = reader.read(source);
		// 获取根节点元素对象
		Element root = document.getRootElement();
		return root;
	}

	/**
	 * 获取当前节点下的节点为name的element迭代器
	 * 
	 * @param element
	 * @param name
	 * @return
	 */
	public static Iterator<Element> getCurrentNameIeraIterator(Element element,
			String name) {
		return element.elementIterator(name);
	}

	/**
	 * 获取该节点下的所有的属性map
	 * 
	 * @param element
	 * @return
	 */
	public static Map<String, String> getAttributeMap(Element element) {
		Map<String, String> map = new HashMap<String, String>();
		// 首先获取当前节点的所有属性节点
		List<Attribute> list = element.attributes();
		// 遍历属性节点
		for (Attribute attribute : list) {
			/*
			 * System.out.println("属性" + attribute.getName() + ":" +
			 * attribute.getValue());
			 */
			map.put(attribute.getName(), attribute.getValue());
		}
		return map;
	}



}
