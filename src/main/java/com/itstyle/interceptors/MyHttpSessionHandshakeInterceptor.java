package com.itstyle.interceptors;

import com.itstyle.handler.MyTextWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Slf4j
public class MyHttpSessionHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    //握手前
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            String username = servletRequest.getServletRequest().getParameter("username");
            String passType = servletRequest.getServletRequest().getParameter("pass_type");
            //必须提供 username,action 请求参数，否则不允许连接
            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(passType)){
                return false;
            }
            attributes.put(MyTextWebSocketHandler.WEB_SOCKET_USERNAME, username);
            attributes.put(MyTextWebSocketHandler.WEB_SOCKET_PASS_TYPE, passType);
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    //握手后
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
