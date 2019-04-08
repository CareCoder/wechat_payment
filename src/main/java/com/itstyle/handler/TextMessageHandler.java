package com.itstyle.handler;

import com.google.gson.Gson;
import com.itstyle.common.WebSocketData;
import com.itstyle.common.WebSocketUserInfo;
import com.itstyle.domain.car.manager.enums.WebSocketAction;
import com.itstyle.domain.caryard.EquipmentStatus;
import com.itstyle.service.ExternalInterfaceService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextMessageHandler {
    static void uploadEquipmentStatus(String userName, WebSocketData webSocketData, ExternalInterfaceService service, Gson gson) {
        EquipmentStatus equipmentStatus = gson.fromJson(gson.toJson(webSocketData.getData()), EquipmentStatus.class);
        log.info("uploadEquipmentStatus userName = {}, equipmentStatus = {}",userName, gson.toJson(equipmentStatus));
        service.uploadEquipmentStatus(equipmentStatus);
    }

    static void forwardMsgToOthers(String userName, WebSocketData webSocketData, Gson gson) {
        log.info("forwardMsgToOthers userName = {}, data = {}", gson, gson.toJson(webSocketData));
        WebSocketUserInfo webSocketUserInfo = MyTextWebSocketHandler.users.get(userName);
        MyTextWebSocketHandler.users.values().stream()
                .filter(e -> {
                    if (userName.equals(e.userName)) {
                        return false;
                    }
                    return e.passType == webSocketUserInfo.passType;
                })
                .forEach(e -> MyTextWebSocketHandler.sendMessageToUser(e.userName, gson.toJson(webSocketData)));
    }

    public static void updateLcdInfo(ExternalInterfaceService externalInterfaceService, Gson gson) {
        log.info("updateLcdInfo all user");
        WebSocketData data = new WebSocketData();
        data.setAction(WebSocketAction.UPDATE_LCD_INFO);
        data.setData(externalInterfaceService.getImageDisplay());
        MyTextWebSocketHandler.sendMessageToAllUser(gson.toJson(data));
    }


    static void updateLcdInfo(String userName, ExternalInterfaceService externalInterfaceService, Gson gson) {
        log.info("updateLcdInfo userName = {}", userName);
        WebSocketData data = new WebSocketData();
        data.setAction(WebSocketAction.UPDATE_LCD_INFO);
        data.setData(externalInterfaceService.getImageDisplay());
        MyTextWebSocketHandler.sendMessageToUser(userName, gson.toJson(data));
    }
}
