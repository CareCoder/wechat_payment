package com.itstyle.domain.car.manager.enums;

public enum WebSocketAction {
    REMOTE_GATE, //远程开闸 s-c
    CLEAR_PARKING_LOT, //清理车位 s-c
    TEMPCARINFO_PAYMENT_CONFIRM,//临时车收费确认 s-c
    FORWARD_MSG, //转发消息 c-s
    STATUS_MSG, //状态信息 c-s
    UPDATE_LCD_INFO, //更新LCD信息 s-c
    UPDATE_PARKING_SETUP, //车场信息设置更新通知 s-c
    UPDATE_ACCESS_AUTHORITY, // 出入通道和出入权限更新通知 s-c
}
