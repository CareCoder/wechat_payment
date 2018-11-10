package com.itstyle.control;

import com.itstyle.common.YstCommon;
import com.itstyle.domain.car.manager.BanListManager;
import com.itstyle.domain.car.manager.Fastigium;
import com.itstyle.service.GlobalSettingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    }
}
