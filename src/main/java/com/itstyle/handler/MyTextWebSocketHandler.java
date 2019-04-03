package com.itstyle.handler;

import com.google.gson.Gson;
import com.itstyle.common.WebSocketData;
import com.itstyle.common.WebSocketUserInfo;
import com.itstyle.domain.car.manager.enums.PassType;
import com.itstyle.domain.car.manager.enums.WebSocketAction;
import com.itstyle.service.ExternalInterfaceService;
import com.itstyle.utils.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class MyTextWebSocketHandler extends TextWebSocketHandler {
    public static final String WEB_SOCKET_USERNAME = "WEB_SOCKET_USERNAME";
    public static final String WEB_SOCKET_PASS_TYPE = "WEB_SOCKET_PASS_TYPE";

    @Resource
    private Gson gson;
    @Resource
    private ExternalInterfaceService externalInterfaceService;

    static final Map<String, WebSocketUserInfo> users = new ConcurrentHashMap<>();

    public static final Map<String, Long> pingPoneMap = new ConcurrentHashMap<>();

    //处理文本消息
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String userName = (String) session.getAttributes().get(MyTextWebSocketHandler.WEB_SOCKET_USERNAME);
        pingPoneMap.put(userName, System.currentTimeMillis());
        String msg = message.getPayload();
        log.info("user " + userName + " send：" + msg);
        WebSocketData webSocketData = gson.fromJson(msg, WebSocketData.class);
        WebSocketAction action = webSocketData.getAction();
        if (action == WebSocketAction.FORWARD_MSG) {
            //向其他同样类型通道转发消息
            TextMessageHandler.forwardMsgToOthers(userName, webSocketData, gson);
        } else if (action == WebSocketAction.STATUS_MSG) {
            TextMessageHandler.uploadEquipmentStatus(userName, webSocketData, externalInterfaceService, gson);
        } else {
            MyTextWebSocketHandler.sendMessageToUser(userName, "received");
        }
    }

    //连接建立后处理
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        String username = (String) session.getAttributes().get(WEB_SOCKET_USERNAME);
        String passType = (String) session.getAttributes().get(WEB_SOCKET_PASS_TYPE);
        WebSocketUserInfo webSocketUserInfo = new WebSocketUserInfo();
        webSocketUserInfo.passType = PassType.valueOf(passType);
        webSocketUserInfo.userName = username;
        webSocketUserInfo.webSocketSession = session;
        users.put(username, webSocketUserInfo);
        log.info("afterConnectionEstablished username = {}", username);
        pingPoneMap.put(username, System.currentTimeMillis());
        session.sendMessage(new TextMessage("connect success"));
    }

    //连接关闭后处理
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        String username = (String) session.getAttributes().get(WEB_SOCKET_USERNAME);
        //设备下线
        externalInterfaceService.offlineEquipmentStatus(username);
        //移除用户
        users.remove(username);
        log.info("afterConnectionClosed username = {}", username);
    }

    //抛出异常时处理
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        String username = (String) session.getAttributes().get(WEB_SOCKET_USERNAME);
        if(session.isOpen())
            session.close();
        users.remove(username);
        log.error("Web Socket 发生异常！username = "+ username, exception);
    }

    //是否支持局部消息
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 给某个用户发送消息
     */
    private static int sendMessageToUser(String username, TextMessage message) {
        WebSocketSession user = users.get(username).webSocketSession;
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
    private static void sendMessageToAllUser(TextMessage message) {
        for (WebSocketUserInfo webSocketUserInfo : users.values()) {
            doSendMessage(webSocketUserInfo.webSocketSession, message);
        }
        log.info("send mesage to all：" + message);
    }

    /**
     * 给所有在线用户发送消息
     */
    public static void sendMessageToAllUser(String message) {
        sendMessageToAllUser(new TextMessage(message));
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

    public static void closeSession(String username) {
        try {
            pingPoneMap.remove(username);
            users.get(username).webSocketSession.close();
        } catch (IOException e) {
            log.error("MyTextWebSocketHandler close error = ",e);
        }
    }
}
