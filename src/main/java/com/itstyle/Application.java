package com.itstyle;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {
	private static final Logger logger = Logger.getLogger(Application.class);

//	@RequestMapping("/error")
//	public String error() {
//		return "/error/404";
//	}

	public static void main(String[] args) throws InterruptedException, IOException {
		SpringApplication.run(Application.class, args);
		logger.info("支付项目启动 ");
	}

}