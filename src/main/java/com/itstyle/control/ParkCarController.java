package com.itstyle.control;

import com.google.gson.Gson;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.park.ParkCar;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.service.ParkCarService;
import com.itstyle.utils.WxPayUtil;
import com.itstyle.utils.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/park")
public class ParkCarController {
    private static final Logger log = LoggerFactory.getLogger(ParkCarController.class);
    private static Gson gson = new Gson();

    @Resource
    private ParkCarService parkCarService;

    @RequestMapping("/uploadBill")
    @ResponseBody
    public Response uploadBill(String mcNo, String carNo, Long operTime, Integer fee, String openId, Long enterTime) {
        int status = parkCarService.uploadBill(mcNo, carNo, operTime, fee, openId, enterTime);
        return Response.build(status, null, null);
    }

    @RequestMapping("/uploadCarInfo")
    @ResponseBody
    public Response uploadInfo(@RequestParam String openId, @RequestParam String carNo) {
        int status = parkCarService.uploadInfo(carNo, openId);
        return Response.build(status, null, null);
    }

    @RequestMapping("/uploadCarInfo.html")
    public String uploadInfoUI(HttpServletRequest request, String openId) {
        if(StringUtils.isEmpty(openId)){
            return "/error/500";
        }
        request.setAttribute("openId", openId);
        return "uploadCarInfo";
    }

    @RequestMapping("/polling")
    @ResponseBody
    public String polling(String mcNo) {
        Response response;
        List<ParkCar> parkCars = new ArrayList<>();
        parkCars.addAll(parkCarService.polling(mcNo));
        List<ParkCar> parkCars1 = parkCarService.pollAll();
        parkCars.addAll(parkCars1);
        response = Response.build(Status.NORMAL, null, gson.toJsonTree(parkCars));
        return gson.toJson(response);
    }

    @RequestMapping("/order")
    @ResponseBody
    public String order(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String code = request.getParameter("code");
        String openId = null;
        try {
            openId = (String) WxPayUtil.getOpenIdByCode(code).get("openid");
        } catch (IOException e) {
            log.error("[ParkCarController] order error", e);
        }
        return openId;
    }
}
