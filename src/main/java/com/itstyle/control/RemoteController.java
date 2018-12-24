package com.itstyle.control;

import com.google.gson.Gson;
import com.itstyle.common.WebSocketData;
import com.itstyle.domain.car.manager.enums.GateOperType;
import com.itstyle.domain.car.manager.enums.WebSocketAction;
import com.itstyle.domain.caryard.ResponseAccessType;
import com.itstyle.handler.MyTextWebSocketHandler;
import com.itstyle.service.AccessTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 远程控制
 */
@Controller
@Slf4j
@RequestMapping("/remote")
public class RemoteController {
    @Resource
    private AccessTypeService accessTypeService;
    @Resource
    private Gson gson;

    @GetMapping("/gate.html")
    public String geteHtml(Model model) {
        List<ResponseAccessType> accessTypes = accessTypeService.listNoPage();
        model.addAttribute("accessTypes", accessTypes);
        return "/backend/remote-gate";
    }

    @PostMapping("/gate/oper")
    @ResponseBody
    public int gateOper(String passName, GateOperType gateOperType) {
        WebSocketData data = new WebSocketData();
        data.setAction(WebSocketAction.REMOTE_GATE);
        data.setData(gateOperType.toString());
        return MyTextWebSocketHandler.sendMessageToUser(passName, gson.toJson(data));
    }
}
