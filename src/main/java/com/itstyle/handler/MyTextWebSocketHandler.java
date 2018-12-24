package com.itstyle.handler;

import com.itstyle.utils.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MyTextWebSocketHandler extends TextWebSocketHandler {
    public static final String WEB_SOCKET_USERNAME = "WEB_SOCKET_USERNAME";

    private static final Map<String, WebSocketSession> users = new ConcurrentHashMap<>();

    //处理文本消息
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String userName = (String) session.getAttributes().get(WEB_SOCKET_USERNAME);
        log.trace("user " + userName + " send：" + message);
        try {
            session.sendMessage(new TextMessage("received"));
        } catch (IOException e) {
            log.error("doSendMessage ",e);
        }
    }

    //连接建立后处理
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        String username = (String) session.getAttributes().get(WEB_SOCKET_USERNAME);
        users.put(username, session);
        session.sendMessage(new TextMessage("connect success"));
    }

    //连接关闭后处理
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String username = (String) session.getAttributes().get(WEB_SOCKET_USERNAME);
        users.remove(username);
        super.afterConnectionClosed(session, status);
    }

    //抛出异常时处理
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        String username = (String) session.getAttributes().get(WEB_SOCKET_USERNAME);
        if(session.isOpen())
            session.close();
        users.remove(username);
        log.error("Web Socket 发生异常！");
    }


    //是否支持局部消息
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 给某个用户发送消息
     */
    public static int sendMessageToUser(String username, TextMessage message) {
        WebSocketSession user = users.get(username);
        if (user == null) {
            return Status.USER_OFFLINE;
        }
        doSendMessage(user, message);
        log.info("send （" + username + "）one message：" + message);
        return Status.NORMAL;
    }

    /**
     * 给某个用户发送消息
     */
    public static int sendMessageToUser(String username, String message) {
        return sendMessageToUser(username, new TextMessage(message));
    }

    /**
     * 给所有在线用户发送消息
     */
    public static void sendMessageToAllUser(TextMessage message) {
        for (WebSocketSession user : users.values()) {
            doSendMessage(user, message);
        }
        log.info("send mesage to all：" + message);
    }

    private static void doSendMessage(WebSocketSession user, TextMessage message){
        try {
            if (null != user && user.isOpen()) {
                user.sendMessage(message);
            }
        } catch (IOException e) {
            log.error("doSendMessage ",e);
        }
    }
}
