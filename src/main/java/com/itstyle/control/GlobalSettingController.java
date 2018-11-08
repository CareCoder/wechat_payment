package com.itstyle.control;

import com.itstyle.common.YstCommon;
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
    private GlobalSettingService fastigiumService;

    @GetMapping("/get/fastigium")
    public String fastigium(Model model) {
        model.addAttribute("fastigium", fastigiumService.get(YstCommon.FASTIGIUM_KEY, Fastigium.class));
        return "/backend/fastigium";
    }

    @PostMapping("/set/fastigium")
    @ResponseBody
    public void add(Fastigium param) {
        fastigiumService.set(YstCommon.FASTIGIUM_KEY, param);
    }
}
