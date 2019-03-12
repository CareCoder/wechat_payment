package com.itstyle.handler;

import com.google.gson.Gson;
import com.itstyle.common.WebSocketData;
import com.itstyle.common.WebSocketUserInfo;
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
        log.info("user " + userName + " send：" + msg);
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
        WebSocketUserInfo webSocketUserInfo = MyTextWebSocketHandler.users.get(userName);
        MyTextWebSocketHandler.users.values().stream()
                .filter(e -> {
                    if (userName.equals(e.userName)) {
                        return false;
                    }
                    return e.passType == webSocketUserInfo.passType;
                })
                .forEach(e -> MyTextWebSocketHandler.sendMessageToUser(e.userName, gson.toJson(webSocketData.getData())));
    }
}
