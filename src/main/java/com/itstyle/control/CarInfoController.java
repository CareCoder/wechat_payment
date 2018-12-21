package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.common.SystemLoggerHelper;
import com.itstyle.domain.car.manager.CarInfo;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.service.CarInfoService;
import com.itstyle.service.MonthCarInfoService;
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
    @Resource
    private MonthCarInfoService monthCarInfoService;
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
        CarInfo byCarNum1 = carInfoService.getByCarNum(carInfo.getCarNum());
        MonthCarInfo byCarNum2 = monthCarInfoService.getByCarNum(carInfo.getCarNum());
        if(byCarNum1!=null){
            if (byCarNum1.getIsBlackList() !=null && byCarNum1.getIsBlackList()){
                return "已是黑名单车辆";
            }else{
                return "已是免费车辆";
            }
        }
        if (byCarNum2!=null){
            if (byCarNum2.getIsMonth()!=null&&byCarNum2.getIsMonth()){
                return "已是月租车";
            }
        }
        carInfoService.save(carInfo);
        SystemLoggerHelper.log("添加", "添加车辆管理信息:" + carInfo.getCarNum());
        return "添加成功";
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
        CarInfo byCarNum1 = carInfoService.getByCarNum(carInfo.getCarNum());
        MonthCarInfo byCarNum2 = monthCarInfoService.getByCarNum(carInfo.getCarNum());
        if(byCarNum1!=null){
            if (byCarNum1.getIsBlackList() !=null && byCarNum1.getIsBlackList()){
                return "已是黑名单车辆";
            }else{
                return "已是免费车辆";
            }
        }
        if (byCarNum2!=null){
            if (byCarNum2.getIsMonth()!=null&&byCarNum2.getIsMonth()){
                return "已是月租车";
            }
        }
        carInfoService.update(carInfo);
        SystemLoggerHelper.log("更新", "更新车辆信息"+carInfo.getCarNum());
        return "修改成功";
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
