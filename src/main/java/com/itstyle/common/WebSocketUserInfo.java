package com.itstyle.common;

import com.itstyle.domain.car.manager.enums.PassType;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketUserInfo {
    public String userName;
    public PassType passType;
    public WebSocketSession webSocketSession;
}
