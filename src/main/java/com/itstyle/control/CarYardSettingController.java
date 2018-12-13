package com.itstyle.control;

import com.google.gson.Gson;
import com.itstyle.common.PageResponse;
import com.itstyle.common.SystemLoggerHelper;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.car.manager.FixedCarManager;
import com.itstyle.domain.caryard.*;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;
import com.itstyle.service.GlobalSettingService;
import com.itstyle.service.PassPermissionService;
import com.itstyle.utils.BeanUtilIgnore;
import com.itstyle.utils.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        model.addAttribute("car_yard_name", carYardName);
        return "/backend/caryard-name";
    }

    @PostMapping("/name/save")
    @ResponseBody
    public Response setCarYardName(CarYardName carYardName) {
        globalSettingService.set(YstCommon.CAR_YARD_NAME, carYardName);
        SystemLoggerHelper.log("修改", "车场信息重新设置");
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/permission/page")
    public String getCarPassPermissionPage(Model model) {
        wrapModel(model);
        return "/backend/pass-permission";
    }

    @GetMapping("/permission/list")
    @ResponseBody
    public PageResponse<ResponsePassCarStatus> getCarPassPermissionData(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                        @RequestParam(value = "limit", required = false, defaultValue = "20") int limit) {
        return passPermissionService.list(page, limit);
    }

    @GetMapping("/pass-permission-add.html")
    public String addCarPassPermissionPage(Model model) {
        wrapModel(model);
        return "/backend/pass-permission-add";
    }

    @PostMapping("/permission/add")
    @ResponseBody
    public Response addCarPassPermission(PassCarStatus passCarStatus) {
        AssertUtil.assertNotNull(passCarStatus, () -> new BusinessException("pass cat status is null"));
        passPermissionService.save(passCarStatus);
        SystemLoggerHelper.log("保存", "创建出入权限");
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/pass-permission-edit.html")
    public String editCarPassPermissionPage(Long id, Model model) {
        PassCarStatus passCarStatus = passPermissionService.getById(id);
        AccessType accessType = passPermissionService.getAccessTypeId(passCarStatus.getAccessTypeId());
        ResponsePassCarStatusEdit responsePassCarStatusEdit = new ResponsePassCarStatusEdit();
        BeanUtilIgnore.copyPropertiesIgnoreNull(passCarStatus, responsePassCarStatusEdit);
        responsePassCarStatusEdit.setChannelTypeId(accessType.getChannelTypeId());
        model.addAttribute("pass_car_status", responsePassCarStatusEdit);
        wrapModel(model);
        return "/backend/pass-permission-edit";
    }

    @PostMapping("/permission/edit")
    @ResponseBody
    public Response editCarPassPermission(PassCarStatus passCarStatus) {
        passPermissionService.update(passCarStatus);
        SystemLoggerHelper.log("修改", "修改出入权限");
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/permission/delete/{id}")
    @ResponseBody
    public Response deleteCarPassPermission(@PathVariable("id") Long id) {
        passPermissionService.delete(id);
        SystemLoggerHelper.log("删除", "删除出入权限");
        return Response.build(Status.NORMAL, null, null);
    }

    private void wrapModel(Model model) {
        List<FixedCarManager> f = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        if (f != null && f.size() > 3) {
            model.addAttribute("MONTH_CAR_A", f.get(0));
            model.addAttribute("MONTH_CAR_B", f.get(1));
            model.addAttribute("MONTH_CAR_C", f.get(2));
            model.addAttribute("VIP_CAR", f.get(3));
        }
    }

}
