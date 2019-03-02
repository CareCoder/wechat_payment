package com.itstyle.service;

import com.itstyle.common.YstCommon;
import com.itstyle.domain.msg.resp.TextResponseMessage;
import com.itstyle.task.AssessTokenTask;
import com.itstyle.utils.HttpUtils;
import com.itstyle.utils.MessageUtil;
import com.itstyle.utils.TemplateUtils;
import com.itstyle.utils.enums.QRCodeAction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Service("coreService")
public class CoreService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private AssessTokenTask assessTokenTask;
	@Autowired
	ParkCarService parkCarService;

	private static final String[] array = new String[2];
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			String respContent = "服务号异常，请稍后再试";// 默认返回的文本消息内容
			Map<String, String> requestMap = MessageUtil.parseXml(request); // xml请求解析
			logger.info("打印微信回调的数据" + requestMap);

			String fromUserName = requestMap.get("FromUserName"); // 发送方帐号（open_id）
			String toUserName = requestMap.get("ToUserName"); // 公众帐号
			String msgType = requestMap.get("MsgType"); // 消息类型
			logger.debug("fromUserName=【{}】, toUserName=【{}】, msgType=【{}】",
					fromUserName, toUserName, msgType);
			logger.info("fromUserName=【{}】, toUserName=【{}】, msgType=【{}】",fromUserName, toUserName, msgType);
			// 回复文本消息
			TextResponseMessage textMessage = new TextResponseMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

			// 文本消息
			if (MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(msgType)) {
				// String conent = "<a
				// href=\"http://wxjj.tunnel.mobi/dscp-mobile-demo/registerUser.jsp\">注册新用户/::Q</a>";
				// respContent = conent;
				// textMessage.setMsgType(MessageUtil.TRANSFER_CUSTOMER_SERVICE);
				textMessage.setContent(requestMap.get("Content"));
				respMessage = MessageUtil.textMessageToXml(textMessage);
				logger.info("return respMessage=【{}】", respMessage);
				return respMessage;
			}
			// 图片消息
			else if (MessageUtil.REQ_MESSAGE_TYPE_IMAGE.equals(msgType)) {
				respContent = "您发送的是图片消息！";
			}
			// 地理位置消息
			else if (MessageUtil.REQ_MESSAGE_TYPE_LOCATION.equals(msgType)) {
				respContent = "您发送的是地理位置消息！";
			}
			// 链接消息
			else if (MessageUtil.REQ_MESSAGE_TYPE_LINK.equals(msgType)) {
				respContent = "您发送的是链接消息！";
			}
			// 音频消息
			else if (MessageUtil.REQ_MESSAGE_TYPE_VOICE.equals(msgType)) {
				respContent = "";
			}
			// 事件推送
			else if (MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgType)) {
				String ticket = requestMap.get("Ticket");
				logger.info("Ticket:"+ticket);
				// 事件类型
				String eventType = "";
				if(array[0] == null){
					array[0] = ticket;
					logger.info("array[0]:"+array[0]);
					eventType = requestMap.get("Event");
				}else {
					array[1] = ticket;
					logger.info("array[0]:"+array[0]);
					logger.info("array[1]:"+array[1]);
					if (array[0].equals(array[1])) {
						eventType = "INVALID";
						logger.info("1*****eventType:" + eventType);
					} else {
						array[0] = array[1];
						eventType = requestMap.get("Event");
						logger.info("2*****eventType:" + eventType);
					}
				}

				// 订阅
				if (MessageUtil.EVENT_TYPE_SUBSCRIBE.equals(eventType)) {
					respContent = makeScanEventResp(requestMap, fromUserName);
				}
				// 取消订阅
				else if (MessageUtil.EVENT_TYPE_UNSUBSCRIBE.equals(eventType)) {
					respContent =  "取消关注成功";
				}
				// 自定义菜单点击事件
				else if (MessageUtil.EVENT_TYPE_CLICK.equals(eventType)) {

				} else if (MessageUtil.EVENT_TYPE_SCAN.equals(eventType)) {
					respContent =  makeScanEventResp(requestMap, fromUserName);
				}else if(MessageUtil.QR_CODE_INVALID.equals(eventType)){
					respContent="此二维码已失效";
				}
			}
			textMessage.setContent(respContent);
			respMessage = MessageUtil.textMessageToXml(textMessage);
			logger.debug("return respMessage=【{}】", respMessage);
			logger.info("return respMessage=【{}】", respMessage);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("处理微信发来的请求异常!", e);
		}
		return respMessage;
	}

	/**
	 *
	 * @param requestMap 微信请求
	 * @return 返回默认的空字符串，或者具体是响应（可能是模版消息，可能是普通文本消息） 给用户
	 */
	private String makeScanEventResp(Map<String, String> requestMap, String openId) {
		String args = requestMap.get("EventKey");
		if (StringUtils.isNoneBlank(args)) {// 扫描二维码关注事件
			args = args.replaceAll("qrscene_", "");
			// 发送模板消息
			return parseQRCode(args, openId);
		}
		return "";
	}

	/**
	 *
	 *
	 * @param args 关注的时候二维码传的参数
	 * @param openId 用户id
	 * @return 返回需要给用户返回的信息
 	 */
	private String parseQRCode(String args, String openId) {
		logger.info("[CoreService] parseQRCode args = {} openId = {}", args, openId);
		String rsp = "";
		if (isNum(args)) {
			Integer money = Integer.parseInt(args);
			rsp = result(money,openId);
		} else {
			try {
				String[] split = args.split("_");
				String mcNo = split[0];
				String action = split[1];
				if (action.equals(QRCodeAction.ENTER)) {
					parkCarService.init(mcNo, openId);
					rsp = makeEnterResp(mcNo, openId);
				} else if (action.equals(QRCodeAction.LEAVE)) {
					parkCarService.ready(mcNo, openId, true);
					rsp = makeOrderResp(openId);
				} else if (action.equals(QRCodeAction.READY_LEAVE)) {
					parkCarService.ready(YstCommon.INNER_MC_NO, openId, false);
					rsp = makeUploadCarNoResp(openId);
				}
			} catch (Exception e) {
				return "";
			}
		}
		return rsp;
	}

	private String result(Integer money, String openId){
		 HttpUtils.HttPost("http://isparking.cn/wx/sendRedPacket?total_amount=" + money+"&re_openid="+openId);
		return "";
	}

	/**
	 * 用户场内扫码，给用户生成填写车牌的模版
	 * @param openId
	 * @return
	 */
	private String makeUploadCarNoResp(String openId) {
		logger.info("[CoreService] makeUploadCarNoResp openId = {}", openId);
		TemplateUtils.createUploadInfo(openId, assessTokenTask.getAssessToken());
		return "请点击上述信息，完善车牌信息。";
	}

	/**
	 * 用户准备出场时，给用户生产订单信息
	 */
	private String makeOrderResp(String openId) {
		logger.info("[CoreService] makeOrderResp openId = {}", openId);
//		return TemplateUtils.createOrder(parkCarService.queryPay(openId), assessTokenTask.getAssessToken());
		return "您好！停车费用正在生成！请稍后!";
	}

	/**
	 * 用户准备入场之后返回信息给用户
	 * @param mcNo 设备编码
	 * @param openId 用户id
	 * @return
	 */
	private String makeEnterResp(String mcNo, String openId) {
		logger.info("[CoreService] makeEnterResp mcNo = {}, openId = {}",mcNo, openId);
		return "欢迎入场停车！";
	}

	/**
	 * 判断传入的args是否能转换为数字
	 * @param args
	 * @return true||false
	 */
	private static boolean isNum(String args) {
		try {
			new BigDecimal(args);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}