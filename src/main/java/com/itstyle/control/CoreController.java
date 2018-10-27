package com.itstyle.control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itstyle.service.CoreService;
import com.itstyle.utils.CheckUtil;

@Controller
public class CoreController {
	@Resource
	private CoreService coreService;

	private static final Logger logger = LoggerFactory.getLogger(CoreController.class);

	@RequestMapping(value = "/wechat", method = RequestMethod.GET)
	public void coreGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String signature = request.getParameter("signature");// 微信加密签名
		String timestamp = request.getParameter("timestamp");// 时间戳
		String nonce = request.getParameter("nonce");// 随机数
		String echostr = request.getParameter("echostr");// 随机字符串
		logger.debug("get signature=【{}】, timestamp=【{}】,nonce=【{}】,echostr=【{}】",
				new Object[] { signature, timestamp, nonce, echostr });
		if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
			// 如果校验成功，将得到的随机字符串原路返回
			PrintWriter out = response.getWriter();
			out.print(echostr);
		}

	}

	@RequestMapping(value = "/wechat", method = RequestMethod.POST)
	public void corePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		// 调用核心业务类接收消息、处理消息
		String respMessage = coreService.processRequest(request);
		// 响应消息
		PrintWriter out = response.getWriter();
		out.print(respMessage);
		out.close();
	}
}
