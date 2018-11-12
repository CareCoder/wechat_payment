package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.service.MonthCarInfoService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/monthcarinfo")
public class MonthCarInfoController {
    @Resource
    private MonthCarInfoService monthCarInfoService;

    @GetMapping("/list")
    @ResponseBody
    public PageResponse<MonthCarInfo> list(int page, int limit, String query) {
        return monthCarInfoService.queryLimit(page, limit, query);
    }

    @PostMapping("/add")
    @ResponseBody
    public void add(MonthCarInfo monthCarInfo) {
        monthCarInfoService.add(monthCarInfo);
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable("id") Long id) {
        monthCarInfoService.delete(id);
    }

    @GetMapping("/find/{id}")
    @ResponseBody
    public void find(@PathVariable("id") Long id) {
        monthCarInfoService.findById(id);
    }


    /**
     * 这个接口不得修改 startTime 和 endTime ，如果需要修改需要去续费接口
     */
    @PostMapping("/update")
    @ResponseBody
    public void update(MonthCarInfo monthCarInfo) {
        monthCarInfo.setStartTime(null);
        monthCarInfo.setEndTime(null);
        monthCarInfoService.update(monthCarInfo.getId(), monthCarInfo);
    }

    /**
     * 续费月租车
     */
    @PostMapping("/payment")
    @ResponseBody
    public void payment(@RequestParam Long startTime,
                        @RequestParam Long endTime,
                        @RequestParam Long id) {
        monthCarInfoService.payment(startTime, endTime, id);
    }
}
