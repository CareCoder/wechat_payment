package com.itstyle.utils;

import com.itstyle.common.YstCommon;
import com.itstyle.domain.weixin.RedPackEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.*;

/**
 * 微信红包工具
 */
public class RedPackUtil {

    public static String sendRedPack(String data) {
        StringBuffer message = new StringBuffer();
        try {
            String encodeData = URLEncoder.encode(data,"utf-8");
            //服务商ID
            String mch_id = YstCommon.MCH_ID;
            //获取KeyStore
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            ClassPathResource pathResource = new ClassPathResource("static/certificate/apiclient_cert.p12");
            keyStore.load(pathResource.getInputStream(), mch_id.toCharArray());
            //创建SSL
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mch_id.toCharArray()).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            HttpPost httpost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack");
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(new String(encodeData.toString().getBytes("UTF-8"), "ISO8859-1")));
            System.out.println("executing request" + httpost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpost);
            HttpEntity entity = response.getEntity();
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            if (entity != null) {
                System.out.println("Response content length: " + entity.getContentLength());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    message.append(text);
                }
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("红包返回结果:" + message.toString());
        return message.toString();
    }

    /**
     * 创建红包签名
     *
     * @param redPack 红包实体
     * @return String MD5加密后的值
     */
    public static String createRedPackOrderSign(RedPackEntity redPack) {
        Map<String, String> params = new HashMap<>();
        params.put("act_name", redPack.getActName());
        params.put("client_ip", redPack.getClientIp());
        params.put("mch_billno", redPack.getMchBillNo());
        params.put("mch_id", redPack.getMchId());
        params.put("nonce_str", redPack.getNonceStr());
        params.put("re_openid", redPack.getReOpenid());
        params.put("remark", redPack.getRemark());
        params.put("send_name", redPack.getSendName());
        params.put("total_amount", String.valueOf(redPack.getTotalAmount()));
        params.put("total_num", String.valueOf(redPack.getTotalNum()));
        params.put("wishing", redPack.getWishing());
        params.put("wxappid", redPack.getWxAppid());
        try {
            String url = makeUrlFormMap(params, false, false);
            url += "&key=" + YstCommon.KEY;
            return DigestUtils.md5Hex(url).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串
     *
     * @param paraMap   要排序的Map对象
     * @param urlEncode 是否需要URLENCODE
     * @return keyToLower 是否需要将Key转换为全小写  true:key转化成小写，false:不转化
     * @throws UnsupportedEncodingException 未知字符集异常
     */
    public static String makeUrlFormMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower) throws UnsupportedEncodingException {
        StringBuffer stringBuffer = new StringBuffer();
        List<Map.Entry<String, String>> entryList = new ArrayList<>(paraMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, String>>() {
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        //构造URL
        for (Map.Entry<String, String> entry: entryList) {
            if (!StringUtils.isBlank(entry.getKey())) {
                String key = entry.getKey();
                String val = entry.getValue();
                if (urlEncode) {
                    val = URLEncoder.encode(val, StandardCharsets.UTF_8.name());
                }
                if (keyToLower) {
                    stringBuffer.append(key.toLowerCase()).append("=").append(val);
                } else {
                    stringBuffer.append(key).append("=").append(val);
                }
                stringBuffer.append("&");
            }
        }
        String url = stringBuffer.toString();
        if (!url.isEmpty()) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }
}
