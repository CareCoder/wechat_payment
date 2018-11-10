package com.itstyle.control;

import com.google.gson.Gson;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.caryard.CarYardName;
import com.itstyle.domain.caryard.PassCarStatus;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;
import com.itstyle.service.GlobalSettingService;
import com.itstyle.service.PassPermissionService;
import com.itstyle.utils.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/set")
public class CarYardSettingController {

    private GlobalSettingService globalSettingService;
    private PassPermissionService passPermissionService;

    @Autowired
    public CarYardSettingController(GlobalSettingService globalSettingService,
                                    PassPermissionService passPermissionService) {
        this.globalSettingService = globalSettingService;
        this.passPermissionService = passPermissionService;
    }

    @GetMapping("/name/query")
    public String getCarYardName(Model model) {
        CarYardName carYardName = (CarYardName) globalSettingService.get(YstCommon.CAR_YARD_NAME, CarYardName.class);
        if (carYardName == null) {
            carYardName = new CarYardName();
        }
        model.addAttribute("car_yard_name", carYardName);
        return "/backend/caryard-name";
    }

    @PostMapping("/name/save")
    @ResponseBody
    public Response setCarYardName(CarYardName carYardName) {
        AssertUtil.assertNotNull(carYardName, () -> new BusinessException("car yard name is null"));
        AssertUtil.assertNotEmpty(carYardName.getName(), () -> new BusinessException("车场名称不能为空"));
        AssertUtil.assertNotNull(carYardName.getParkNum(), () -> new BusinessException("车位数不能为空"));
        globalSettingService.set(YstCommon.CAR_YARD_NAME, carYardName);
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/permission/query")
    public String getCarPassPermission(Model model) {
        List<PassCarStatus> list = passPermissionService.list();
        model.addAttribute("data", list);
        return "/backend/pass-permission";
    }
}
