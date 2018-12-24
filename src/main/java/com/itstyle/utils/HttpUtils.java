package com.itstyle.utils;

import java.nio.charset.Charset;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

public class HttpUtils {
	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	public static String HttPost(String url, String param) {
		try {
			HttpPost httppost = new HttpPost(url);
			StringEntity reqEntity = new StringEntity(new String(param.toString().getBytes("UTF-8"), "ISO8859-1"));// 这个处理是为了防止传中文的时候出现签名错误
			httppost.setEntity(reqEntity);
			CloseableHttpClient httpclient = HttpClients.createDefault();
			CloseableHttpResponse response = httpclient.execute(httppost);
			String strResult = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
			return strResult;
		} catch (Exception e) {
			logger.error("发送post请求失败{}", e);
			return null;
		}

	}

	public static HttpHeaders getDownloadHttpHeaders(String fileName) throws Exception{
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/octet-stream");
		headers.add("Connection", "close");
		headers.add("Accept-Ranges", "bytes");
		headers.add("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
		return headers;
	}

}
