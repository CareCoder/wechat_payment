
package com.itstyle.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XmlUtils {
	private static final String HEAD = "head";
	private static final String BODY = "body";

	public static void main(String[] args) throws DocumentException {
		String xml = "<xml><return_code><![CDATA[SUCCESS]]></return_code>\r\n"
				+ "<return_msg><![CDATA[OK]]></return_msg>\r\n" + "<appid><![CDATA[wxd49da7cb176f674b]]></appid>\r\n"
				+ "<mch_id><![CDATA[1271069001]]></mch_id>\r\n"
				+ "<nonce_str><![CDATA[aoNIggasHptDINoh]]></nonce_str>\r\n"
				+ "<sign><![CDATA[75592D30A1A120D42CFD9F7D70F84A69]]></sign>\r\n"
				+ "<result_code><![CDATA[SUCCESS]]></result_code>\r\n"
				+ "<prepay_id><![CDATA[wx102343454828718e51b5ca393208339368]]></prepay_id>\r\n"
				+ "<trade_type><![CDATA[JSAPI]]></trade_type>\r\n" + "</xml>\r\n" + "";
		try {
			System.out.println(Dom2Map(xml).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析XML字符串
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	public static Map<String, Object> parseXmlStr(String xml) throws DocumentException {
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		return parseElement(root);
	}

	/**
	 * 解析Element
	 * 
	 * @param root
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Object> parseElement(Element root) {
		String rootName = root.getName();
		Iterator<Element> rootItor = root.elementIterator();
		Map<String, Object> rMap = new HashMap<String, Object>();
		List<Map<String, Object>> rList = new ArrayList<Map<String, Object>>();
		Map<String, Object> rsltMap = null;
		while (rootItor.hasNext()) {
			Element tmpElement = rootItor.next();
			String name = tmpElement.getName();
			if (rsltMap == null || (!HEAD.equals(name) && !BODY.equals(name) && !tmpElement.isTextOnly())) {
				if (!HEAD.equals(name) && !BODY.equals(name) && !tmpElement.isTextOnly() && rsltMap != null) {
					rList.add(rsltMap);
				}
				rsltMap = new HashMap<String, Object>();
			}
			if (!tmpElement.isTextOnly()) {
				Iterator<Element> headItor = tmpElement.elementIterator();
				while (headItor.hasNext()) {
					Element hElement = headItor.next();
					if (hElement.isTextOnly()) {
						rsltMap.put(hElement.getName(), hElement.getTextTrim());
					} else {
						rsltMap.putAll(parseElement(hElement));
					}
				}
			}
		}
		rList.add(rsltMap);
		rMap.put(rootName, rList);
		return rMap;
	}

	public static Map<String, Object> Dom2Map(String str) throws Exception {
		Document doc = DocumentHelper.parseText(str);
		Map<String, Object> map = new HashMap<String, Object>();
		if (doc == null)
			return map;
		Element root = doc.getRootElement();
		for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
			Element e = (Element) iterator.next();
			List list = e.elements();
			if (list.size() > 0) {
				map.put(e.getName(), Dom2Map(e));
			} else
				map.put(e.getName(), e.getText());
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public static Map Dom2Map(Element e) {
		Map map = new HashMap();
		List list = e.elements();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Element iter = (Element) list.get(i);
				List mapList = new ArrayList();

				if (iter.elements().size() > 0) {
					Map m = Dom2Map(iter);
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(m);
						}
						if (obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(m);
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), m);
				} else {
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(iter.getText());
						}
						if (obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(iter.getText());
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), iter.getText());
				}
			}
		} else
			map.put(e.getName(), e.getText());
		return map;
	}

}
