package com.itstyle.handler;

import com.google.gson.Gson;
import com.itstyle.common.WebSocketData;
import com.itstyle.domain.car.manager.enums.WebSocketAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
public class TextMessageHandler {
    private static Gson gson = new Gson();
    public static void handleTextMessage(WebSocketSession session, TextMessage message) {
        String userName = (String) session.getAttributes().get(MyTextWebSocketHandler.WEB_SOCKET_USERNAME);
        String msg = message.getPayload();
        log.trace("user " + userName + " send：" + msg);
        WebSocketData webSocketData = gson.fromJson(msg, WebSocketData.class);
        WebSocketAction action = webSocketData.getAction();
        if (action == WebSocketAction.FORWARD_MSG) {
            //向其他同样类型通道转发消息
            forwardMsgToOthers(userName, webSocketData);
        }else{
            MyTextWebSocketHandler.sendMessageToUser(userName, "received");
        }
    }

    private static void forwardMsgToOthers(String userName, WebSocketData webSocketData) {
        String enterPassName = "入口";//包含这是入口,否则是出口
        boolean enter = userName.contains(enterPassName);
        MyTextWebSocketHandler.users.keySet().stream()
                .filter(e -> {
                    if (e.equals(userName)) {
                        //不给自己发送
                        return false;
                    }
                    return e.contains(enterPassName) == enter;
                })
                .forEach(e -> MyTextWebSocketHandler.sendMessageToUser(e, gson.toJson(webSocketData.getData())));
    }
}
