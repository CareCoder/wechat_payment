package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.service.MonthCarInfoService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/monthcarinfo")
public class MonthCarInfoController {
    @Resource
    private MonthCarInfoService monthCarInfoService;

    @GetMapping("/monthcarinfo.html")
    public String monthcarinfo() {
        return "/backend/monthcarinfo";
    }

    @GetMapping("/monthcarinfo-edit.html")
    public String monthcarinfoEdit(Long id, Model model) {
        if (id != null) {
            MonthCarInfo monthCarInfo = monthCarInfoService.findById(id);
            model.addAttribute("monthCarInfo", monthCarInfo);
        }
        return "/backend/monthcarinfo-edit";
    }

    @GetMapping("/list")
    @ResponseBody
    public PageResponse<MonthCarInfo> list(int page, int limit, String query) {
        return monthCarInfoService.queryLimit(page, limit, query);
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
     * 如果数据不存在则新增
     */
    @PostMapping("/edit")
    @ResponseBody
    public void edit(MonthCarInfo monthCarInfo) {
        monthCarInfoService.edit(monthCarInfo);
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
