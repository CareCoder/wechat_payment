package com.itstyle.control;

import com.itstyle.domain.car.manager.Fastigium;
import com.itstyle.service.FastigiumService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/fastigium")
public class FastigiumController {
    @Resource
    private FastigiumService fastigiumService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("fastigium", fastigiumService.get());
        return "/backend/fastigium";
    }

    @PostMapping("/set")
    @ResponseBody
    public void add(Fastigium fastigium) {
        fastigiumService.set(fastigium);
    }
}
