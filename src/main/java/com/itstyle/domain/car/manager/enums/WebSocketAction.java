package com.itstyle.domain.car.manager.enums;

public enum WebSocketAction {
    REMOTE_GATE, //远程开闸
    CLEAR_PARKING_LOT, //清理车位
    TEMPCARINFO_PAYMENT_CONFIRM,//临时车收费确认
    FORWARD_MSG, //转发消息
    STATUS_MSG, //状态信息
    UPDATE_LCD_INFO, //更新LCD信息
    UPDATE_PARKING_SETUP //车场信息设置
}
