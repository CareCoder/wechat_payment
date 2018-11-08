package com.itstyle.control;

import com.itstyle.domain.fastigium.Fastigium;
import com.itstyle.service.FastigiumService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/fastigium")
public class FastigiumController {
    @Resource
    private FastigiumService fastigiumService;

    @GetMapping("/get")
    @ResponseBody
    public Fastigium list() {
        return fastigiumService.get();
    }

    @PostMapping("/set")
    @ResponseBody
    public void add(Fastigium fastigium) {
        fastigiumService.set(fastigium);
    }
}
