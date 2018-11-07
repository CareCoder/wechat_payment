package com.itstyle.control;

import com.itstyle.domain.fastigium.Fastigium;
import com.itstyle.service.FastigiumService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/fastigium")
public class FastigiumController {
    @Resource
    private FastigiumService fastigiumService;

    @GetMapping("/list")
    @ResponseBody
    public List<Fastigium> list() {
        return fastigiumService.list();
    }

    @GetMapping("/add")
    @ResponseBody
    public void add(Fastigium fastigium) {
        fastigiumService.add(fastigium);
    }

    @GetMapping("/delete")
    @ResponseBody
    public void delete(Long id) {
        fastigiumService.delete(id);
    }

    @GetMapping("/find/{id}")
    @ResponseBody
    public void findById(@PathVariable("id") Long id) {
        fastigiumService.findById(id);
    }

    @GetMapping("/update")
    @ResponseBody
    public void update(Fastigium fastigium) {
        fastigiumService.update(fastigium.getId(), fastigium);
    }
}
