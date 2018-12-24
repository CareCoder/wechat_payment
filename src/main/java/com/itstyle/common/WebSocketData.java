package com.itstyle.common;

import com.itstyle.domain.car.manager.enums.WebSocketAction;
import lombok.Data;

@Data
public class WebSocketData {
    private WebSocketAction action;
    private Object data;
}
