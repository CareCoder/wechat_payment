package com.itstyle.utils;

import com.itstyle.domain.menu.Menu;
import com.itstyle.domain.msg.resp.TextResponseMessage;
import com.itstyle.domain.templete.TemplateMsg;
import com.itstyle.utils.trust.MyX509TrustManager;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息工具类
 * 
 */
public class MessageUtil
{

    public static final Logger logger = LoggerFactory.getLogger (MessageUtil.class);
    
    /** 返回消息类型：客服. */
    public static final String TRANSFER_CUSTOMER_SERVICE = "transfer_customer_service";

    /** 返回消息类型：文本. */
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";

    /** 返回消息类型：音乐. */
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

    /** 返回消息类型：图文. */
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";

    /** 请求消息类型：文本. */
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";

    /** 请求消息类型：图片. */
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

    /** 请求消息类型：链接. */
    public static final String REQ_MESSAGE_TYPE_LINK = "link";

    /** 请求消息类型：地理位置. */
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

    /** 请求消息类型：音频. */
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

    /** 请求消息类型：推送. */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";

    /** 事件类型：subscribe(订阅). */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /** 事件类型：unsubscribe(取消订阅). */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

    /** 事件类型：CLICK(自定义菜单点击事件). */
    public static final String EVENT_TYPE_CLICK = "CLICK";

    /** 事件类型：VIEW(自定义菜单点击事件). */
    public static final String EVENT_TYPE_VIEW = "VIEW";
    
   /* 扫描二维码事件*/
    public static final String EVENT_TYPE_SCAN= "SCAN";

    /**
     * 解析微信发来的请求（XML）
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings ("unchecked")
    public static Map <String, String> parseXml (HttpServletRequest request) throws Exception
    {
        // 将解析结果存储在HashMap中
        Map <String, String> map = new HashMap <String, String> ();

        // 从request中取得输入流
        InputStream inputStream = request.getInputStream ();
        // 读取输入流
        SAXReader reader = new SAXReader ();
        Document document = reader.read (inputStream);

        logger.debug ("request xml =【{}】", document.asXML ());

        // 得到xml根元素
        Element root = document.getRootElement ();
        // 得到根元素的所有子节点
        List <Element> elementList = root.elements ();

        // 遍历所有子节点
        for (Element e : elementList)
            map.put (e.getName (), e.getText ());

        // 释放资源
        inputStream.close ();
        inputStream = null;

        return map;
    }

    /**
     * 文本消息对象转换成xml
     * 
     * @param textMessage 文本消息对象
     * @return xml
     */
    public static String textMessageToXml (TextResponseMessage textMessage)
    {
        xstream.alias ("xml", textMessage.getClass ());
        return xstream.toXML (textMessage);
    }

    /**
     * 音乐消息对象转换成xml
     * 
     * @param musicMessage 音乐消息对象
     * @return xml
     */
    // public static String musicMessageToXml(MusicMessage musicMessage) {
    // xstream.alias("xml", musicMessage.getClass());
    // return xstream.toXML(musicMessage);
    // }

    /**
     * 图文消息对象转换成xml
     * 
     * @param newsMessage 图文消息对象
     * @return xml
     */
    // public static String newsMessageToXml(NewsMessage newsMessage) {
    // xstream.alias("xml", newsMessage.getClass());
    // xstream.alias("item", new Article().getClass());
    // return xstream.toXML(newsMessage);
    // }

    /**
     * 扩展xstream，使其支持CDATA块
     * 
     * @date 2013-05-19
     */
    private static XStream xstream = new XStream (new XppDriver()
    {
        public HierarchicalStreamWriter createWriter (Writer out)
        {
            return new PrettyPrintWriter(out)
            {
                // 对所有xml节点的转换都增加CDATA标记
                boolean cdata = true;

                public void startNode (String name, @SuppressWarnings ("rawtypes") Class clazz)
                {
                    super.startNode (name, clazz);
                }

                protected void writeText (QuickWriter writer, String text)
                {
                    if (cdata)
                    {
                        writer.write ("<![CDATA[");
                        writer.write (text);
                        writer.write ("]]>");
                    }
                    else
                    {
                        writer.write (text);
                    }
                }
            };
        }
    });

    public static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    /**
     * 创建菜单
     * 
     * @param menu 菜单实例
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public static int createMenu (Menu menu, String accessToken)
    {
        int result = 0;

        // 拼装创建菜单的url
        String url = MENU_CREATE_URL.replace ("ACCESS_TOKEN", accessToken);
        // 将菜单对象转换成json字符串
        String jsonMenu = JSONObject.fromObject (menu).toString ();
        System.out.println(jsonMenu);
        // 调用接口创建菜单
        JSONObject jsonObject = httpRequest (url, "POST", jsonMenu);

        if (null != jsonObject)
        {
            if (0 != jsonObject.getInt ("errcode"))
            {
                result = jsonObject.getInt ("errcode");
                logger.error ("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt ("errcode"),
                              jsonObject.getString ("errmsg"));
            }
        }
        return result;
    }
    
    
    public final static String SEND_TEMPLATE_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
    
    public static int sendTemplateMsg (TemplateMsg scoreMessage , String accessToken)
    {
        int result = 0;

        // 拼装创建菜单的url
        String url = SEND_TEMPLATE_MSG_URL.replace ("ACCESS_TOKEN", accessToken);
        // 将菜单对象转换成json字符串
        String jsonMsg = JSONObject.fromObject (scoreMessage).toString ();
        // 调用接口创建菜单
        JSONObject jsonObject = httpRequest (url, "POST", jsonMsg);

        if (null != jsonObject)
        {
            if (0 != jsonObject.getInt ("errcode"))
            {
                result = jsonObject.getInt ("errcode");
                logger.error ("发送模版消息失败 errcode:{} errmsg:{}", jsonObject.getInt ("errcode"),
                              jsonObject.getString ("errmsg"));
            }
        }
        return result;
    }

    public final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    public static JSONObject httpRequest (String requestUrl, String requestMethod, String outputStr)
    {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer ();
        try
        {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm =
            { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance ("SSL", "SunJSSE");
            sslContext.init (null, tm, new java.security.SecureRandom ());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory ();

            URL url = new URL (requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection ();
            httpUrlConn.setSSLSocketFactory (ssf);

            httpUrlConn.setDoOutput (true);
            httpUrlConn.setDoInput (true);
            httpUrlConn.setUseCaches (false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod (requestMethod);

            if ("GET".equalsIgnoreCase (requestMethod))
                httpUrlConn.connect ();

            // 当有数据需要提交时
            if (null != outputStr)
            {
                OutputStream outputStream = httpUrlConn.getOutputStream ();
                // 注意编码格式，防止中文乱码
                outputStream.write (outputStr.getBytes ("UTF-8"));
                outputStream.close ();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream ();
            InputStreamReader inputStreamReader = new InputStreamReader (inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader (inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine ()) != null)
            {
                buffer.append (str);
            }
            bufferedReader.close ();
            inputStreamReader.close ();
            // 释放资源
            inputStream.close ();
            inputStream = null;
            httpUrlConn.disconnect ();
            jsonObject = JSONObject.fromObject (buffer.toString ());
        }
        catch (ConnectException ce)
        {
            logger.error ("Weixin server connection timed out.");
        }
        catch (Exception e)
        {
            logger.error ("https request error:{}", e);
        }
        return jsonObject;
    }
    
    public static JSONObject httpRequest2 (String requestUrl, String requestMethod, String outputStr)
    {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer ();
        try
        {
            URL url = new URL (requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection ();

            httpUrlConn.setDoOutput (true);
            httpUrlConn.setDoInput (true);
            httpUrlConn.setUseCaches (false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod (requestMethod);

            if ("GET".equalsIgnoreCase (requestMethod))
                httpUrlConn.connect ();

            // 当有数据需要提交时
            if (null != outputStr)
            {
                OutputStream outputStream = httpUrlConn.getOutputStream ();
                // 注意编码格式，防止中文乱码
                outputStream.write (outputStr.getBytes ("UTF-8"));
                outputStream.close ();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream ();
            InputStreamReader inputStreamReader = new InputStreamReader (inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader (inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine ()) != null)
            {
                buffer.append (str);
            }
            bufferedReader.close ();
            inputStreamReader.close ();
            // 释放资源
            inputStream.close ();
            inputStream = null;
            httpUrlConn.disconnect ();
            jsonObject = JSONObject.fromObject (buffer.toString ());
        }
        catch (ConnectException ce)
        {
            logger.error ("Weixin server connection timed out.");
        }
        catch (Exception e)
        {
            logger.error ("https request error:{}", e);
        }
        return jsonObject;
    }
    
    
    public static void httpFileRequest (String requestUrl, String requestMethod, String outputStr, String path)
    {
        try
        {
            URL url = new URL (requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection ();

            httpUrlConn.setDoOutput (true);
            httpUrlConn.setDoInput (true);
            httpUrlConn.setUseCaches (false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod (requestMethod);

            if ("GET".equalsIgnoreCase (requestMethod))
                httpUrlConn.connect ();

            // 当有数据需要提交时
            if (null != outputStr)
            {
                OutputStream outputStream = httpUrlConn.getOutputStream ();
                // 注意编码格式，防止中文乱码
                outputStream.write (outputStr.getBytes ("UTF-8"));
                outputStream.close ();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            OutputStream os = new FileOutputStream(path);
            
            byte [] temp = new byte [1024];
            int i = 0;
            while ((i=inputStream.read(temp)) > 0) {
                os.write(temp, 0, i);
                os.flush();
            }
            inputStream.close();
            os.close();
            httpUrlConn.disconnect();
        }
        catch (ConnectException ce)
        {
            logger.error ("Weixin server connection timed out.");
        }
        catch (Exception e)
        {
            logger.error ("https request error:{}", e);
        }
    }
    
    public static String readJSONString (HttpServletRequest request)
    {
        StringBuilder json = new StringBuilder (300);
        String line = null;
        try
        {
            BufferedReader reader = request.getReader ();
            while ((line = reader.readLine ()) != null)
            {
                json.append (line);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException ("读取Json字符串出现异常!", e);
        }
        return json.toString ();
    }
}