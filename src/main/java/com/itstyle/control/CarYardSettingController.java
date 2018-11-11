package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.caryard.CarYardName;
import com.itstyle.domain.caryard.EquipmentStatus;
import com.itstyle.domain.caryard.PassCarStatus;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;
import com.itstyle.service.GlobalSettingService;
import com.itstyle.service.PassPermissionService;
import com.itstyle.utils.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
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

    @PostMapping("/permission/edit")
    @ResponseBody
    public Response editCarPassPermission(@RequestParam Map<String, String> map) {
        log.info("[CarYardSettingController] request param is [{}]", map);
        List<PassCarStatus> list = passPermissionService.list();
        List<PassCarStatus> collect = list.stream().map(passCarStatus -> {
            passCarStatus.setStatus(Integer.parseInt(map.get(passCarStatus.getName())));
            return passCarStatus;
        }).collect(Collectors.toList());
        passPermissionService.update(collect);
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/equipment/page")
    public String equipmentPage() {
        return "/backend/equipment-info";
    }

    @GetMapping("/equipment/query")
    @ResponseBody
    public PageResponse<EquipmentStatus> equipmentList(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                       @RequestParam(value = "limit", required = false, defaultValue = "20") int limit) {
        return passPermissionService.equipmentList(page, limit);
    }
}
