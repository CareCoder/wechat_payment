package com.itstyle.control;

import com.itstyle.domain.carinfo.CarInfo;
import com.itstyle.service.CarInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/carinfo")
public class CarInfoController {
    @Resource
    private CarInfoService carInfoService;

    @RequestMapping("/list")
    @ResponseBody
    public List<CarInfo> list() {
        return carInfoService.list();
    }

    @RequestMapping("/get/{id}")
    @ResponseBody
    public CarInfo getById(@PathVariable("id") Long id) {
        return carInfoService.getById(id);
    }

    @RequestMapping("/find/carnum/{carNum}")
    @ResponseBody
    public CarInfo getById(@PathVariable("carNum") String carNum) {
        return carInfoService.getByCarNum(carNum);
    }

    @RequestMapping("/save")
    public void save(CarInfo carInfo) {
        carInfoService.save(carInfo);
    }

    @RequestMapping("/delete")
    public void save(Long id) {
        carInfoService.delete(id);
    }

    @RequestMapping("/update")
    public void update(CarInfo carInfo) {
        carInfoService.update(carInfo);
    }

    @RequestMapping("/carinfo.html")
    public String carinfoUI(String type, Model model) {
        model.addAttribute("type", type);
        return "/backend/carinfo";
    }

    @RequestMapping("/carinfo-add.html")
    public String carinfo_addUI(String type, Model model) {
        model.addAttribute("type", type);
        return "/backend/carinfo-add";
    }
}
