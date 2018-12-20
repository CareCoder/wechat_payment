package com.itstyle.config;

import com.itstyle.handler.MyTextWebSocketHandler;
import com.itstyle.interceptors.MyHttpSessionHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig {
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry
                .addHandler(myTextWebSocketHandler(), "/hrzx/webSocket")
                .addInterceptors(myHttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*");
        //可指定多个跨域，如果无需限制可使用 *
    }

    //用于定义 WebSocket 的消息处理
    @Bean
    public WebSocketHandler myTextWebSocketHandler() {
        return new MyTextWebSocketHandler();
    }

    //用于处理 WebSocket 连接
    @Bean
    public HandshakeInterceptor myHttpSessionHandshakeInterceptor(){
        return new MyHttpSessionHandshakeInterceptor();
    }
}
