package com.itstyle.task;

import com.itstyle.common.YstCommon;
import com.itstyle.utils.MessageUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Configuration
@EnableScheduling
@Lazy(false)
public class AssessTokenTask {
	private static final Logger log = LoggerFactory.getLogger(AssessTokenTask.class);

	private String assessToken;

	private String ticket;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getAssessToken() {
		return assessToken;
	}

	public void setAssessToken(String assessToken) {
		log.info("[AssessTokenTask] setAssessToken token = {}", assessToken);
		this.assessToken = assessToken;
	}

	@PostConstruct
	public void task() {
		JSONObject token = new JSONObject();
		try {
			token = MessageUtil
					.httpRequest("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
							+ YstCommon.APPID + "&secret=" + YstCommon.APP_SECRET, "GET", null);

			JSONObject ticketStr = MessageUtil
					.httpRequest("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
							+ token.getString("access_token") + "&type=jsapi", "GET", null);

			setAssessToken(token.getString("access_token"));
			setTicket(ticketStr.getString("ticket"));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[AssessTokenTask] error token = {}", token.toString());
		}

	}

	@Scheduled(cron = "0 0 */1 * * ?")
	public void exeJob() {
		log.info("[AssessTokenTask] exeJob");
		task();
	}
}
