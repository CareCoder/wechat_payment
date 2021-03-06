package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.common.SystemLoggerHelper;
import com.itstyle.domain.car.manager.CarInfo;
import com.itstyle.service.CarInfoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/carinfo")
public class CarInfoController {
    @Resource
    private CarInfoService carInfoService;

    @RequestMapping("/list")
    @ResponseBody
    public PageResponse<CarInfo> list(Integer page, Integer limit, String type, String query) {
        return carInfoService.list(page, limit, type, query);
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

    @PostMapping("/save")
    @ResponseBody
    public String save(CarInfo carInfo, HttpServletResponse httpResponse) {
        String result = carInfoService.save(carInfo);
        SystemLoggerHelper.log("添加", "添加车辆管理信息:" + carInfo.getCarNum());
        return result;
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable ("id") Long id) {
        CarInfo byId = carInfoService.getById(id);
        carInfoService.delete(id);
        SystemLoggerHelper.log("删除", "删除车辆管理信息:" + byId.getCarNum());
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(CarInfo carInfo) {
        String result = carInfoService.update(carInfo);
        SystemLoggerHelper.log("更新", "更新车辆信息"+carInfo.getCarNum());
        return result;
    }

    @RequestMapping("/carinfo/{type}")
    public String carinfoUI(@PathVariable("type") String type, Model model) {
        model.addAttribute("type", type);
        return "/backend/carinfo";
    }

    @RequestMapping("/carinfo-add.html")
    public String carinfo_addUI(String type, Model model) {
        model.addAttribute("type", type);
        return "/backend/carinfo-add";
    }

    @RequestMapping("/carinfo-edit.html")
    public String carinfo_editUI(Long id, Model model) {
        CarInfo carInfo = carInfoService.getById(id);
        model.addAttribute("car_info", carInfo);
        return "/backend/carinfo-edit";
    }
}
