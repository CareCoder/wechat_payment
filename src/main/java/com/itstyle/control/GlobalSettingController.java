package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.common.SystemLoggerHelper;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.car.manager.BanListManager;
import com.itstyle.domain.car.manager.Fastigium;
import com.itstyle.domain.car.manager.FixedCarManager;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.service.GlobalSettingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@RequestMapping("/gs")
public class GlobalSettingController {
    @Resource
    private GlobalSettingService globalSettingService;

    @GetMapping("/get/fastigium")
    public String fastigiumGet(Model model) {
        model.addAttribute("fastigium", globalSettingService.get(YstCommon.FASTIGIUM_KEY, Fastigium.class));
        return "/backend/fastigium";
    }

    @PostMapping("/set/fastigium")
    @ResponseBody
    public void fastigiumSet(Fastigium param) {
        globalSettingService.set(YstCommon.FASTIGIUM_KEY, param);
        SystemLoggerHelper.log("配置", "配置高峰期管理");
    }

    @GetMapping("/get/banlist")
    public String banlistSet(Model model) {
        model.addAttribute("banlist", globalSettingService.get(YstCommon.BANLISTMANAGER_KEY, BanListManager.class));
        return "/backend/banlist";
    }

    @PostMapping("/set/banlist")
    @ResponseBody
    public void banlistGet(BanListManager param) {
        globalSettingService.set(YstCommon.BANLISTMANAGER_KEY, param);
        SystemLoggerHelper.log("配置", "配置禁入车辆");
    }


    /***********************fixedcar**************************/

    @GetMapping("/get/fixedcar")
    public String fixedcarGet() {
        return "/backend/fixedcar";
    }

    @GetMapping("/edit/fixedcar")
    public String fixedcarGet(CarType carType, Model model) {
        List<FixedCarManager> f = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        AtomicReference<FixedCarManager> fixedCarManager = new AtomicReference<>();
        f.forEach(e -> {
            if (e.getCarType() == carType) {
                fixedCarManager.set(e);
            }
        });
        model.addAttribute("fixedCarManager", fixedCarManager.get());
        return "/backend/fixedcar-edit";
    }

    @PostMapping("/set/fixedcar")
    @ResponseBody
    public void fixedcarGet(FixedCarManager param) {
        List<FixedCarManager> f = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        for (int i = 0; i < f.size(); i++) {
            if (f.get(i).getCarType() == param.getCarType()) {
                f.set(i, param);
            }
        }
        globalSettingService.set(YstCommon.FIXEDCARMANAGER_KEY, f);
        SystemLoggerHelper.log("更新", "更新固定车辆名称");
    }

    @GetMapping("/get/fixedcar/data")
    @ResponseBody
    public PageResponse<FixedCarManager> fixedcarGetData() {
        List<FixedCarManager> f = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        //如果初次启动系统没有数据则生成默认数据并返回
        if (f == null) {
            f = FixedCarManager.defaultList();
            globalSettingService.set(YstCommon.FIXEDCARMANAGER_KEY, f);
        }
        return new PageResponse<>(0, "", (long) f.size(), f);
    }

    @RequestMapping("/get/specialcar")
    public String specialcarGet(Model model) {
        String keyWords = (String) globalSettingService.get(YstCommon.SPECAL_CAR, String.class);
        model.addAttribute("keyWords", keyWords);
        return "/backend/special-car";
    }

    @RequestMapping("/set/specialcar")
    @ResponseBody
    public void specialcarSet(String keyWords) {
        globalSettingService.set(YstCommon.SPECAL_CAR, keyWords);
        SystemLoggerHelper.log("更新", "更新特殊车辆关键字");
    }
}
