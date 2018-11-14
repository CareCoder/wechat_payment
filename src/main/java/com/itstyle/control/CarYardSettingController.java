package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.caryard.CarYardName;
import com.itstyle.domain.caryard.EquipmentStatus;
import com.itstyle.domain.caryard.PassCarStatus;
import com.itstyle.domain.caryard.ResponsePassCarStatus;
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

    @GetMapping("/permission/page")
    public String getCarPassPermissionPage() {
        return "/backend/pass-permission";
    }

    @GetMapping("/permission/list")
    @ResponseBody
    public PageResponse<ResponsePassCarStatus> getCarPassPermissionData(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                        @RequestParam(value = "limit", required = false, defaultValue = "20") int limit) {
        return passPermissionService.list(page, limit);
    }

    @GetMapping("/pass-permission-add.html")
    public String addCarPassPermissionPage() {
        return "/backend/pass-permission-add";
    }

    @PostMapping("/permission/add")
    @ResponseBody
    public Response addCarPassPermission(PassCarStatus passCarStatus) {
        AssertUtil.assertNotNull(passCarStatus, () -> new BusinessException("pass cat status is null"));
        AssertUtil.assertNotEmpty(passCarStatus.getChannelName(), () -> new BusinessException("通道名称不能为空"));
        AssertUtil.assertNotNull(passCarStatus.getChannelTypeId(), () -> new BusinessException("通道类型不能为空"));
        passPermissionService.save(passCarStatus);
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/pass-permission-edzit.html")
    public String editCarPassPermissionPage(Long id, Model model) {
        PassCarStatus passCarStatus = passPermissionService.getById(id);
        model.addAttribute("pass_car_status", passCarStatus);
        return "/backend/pass-permission-edit";
    }

    @PostMapping("/permission/edit")
    @ResponseBody
    public Response editCarPassPermission(PassCarStatus passCarStatus) {
        passPermissionService.update(passCarStatus);
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/permission/delete/{id}")
    @ResponseBody
    public Response deleteCarPassPermission(@PathVariable("id") Long id) {
        passPermissionService.delete(id);
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
