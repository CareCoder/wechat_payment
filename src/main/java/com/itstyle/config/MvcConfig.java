package com.itstyle.config;

import com.itstyle.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig {
    @Value("${filePath}")
    private String filePath;

    @Value("${fileUrlPrefix}")
    private String fileUrlPrefix;

    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler(fileUrlPrefix + "**").addResourceLocations("file:" + filePath);
                super.addResourceHandlers(registry);
            }
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")
                        .excludePathPatterns("/", "/backend/login.html", "/account/login", "/carnum/**", "/external/**",
                                "/external/version/**", "/external/version/download/**","/file/**","/wx/**","/wechat/**","/park/**");
            }
        };
    }
}
