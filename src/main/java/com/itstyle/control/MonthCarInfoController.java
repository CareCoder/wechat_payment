package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.account.Account;
import com.itstyle.domain.car.manager.FixedCarManager;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.service.GlobalSettingService;
import com.itstyle.service.MonthCarInfoService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/monthcarinfo")
public class MonthCarInfoController {
    @Resource
    private MonthCarInfoService monthCarInfoService;
    @Resource
    private GlobalSettingService globalSettingService;

    @GetMapping("/monthcarinfo.html")
    public String monthcarinfo() {
        return "/backend/monthcarinfo";
    }

    @GetMapping("/monthcarinfo-edit.html")
    public String monthcarinfoEdit(Long id, Model model) {
        if (id != null) {
            MonthCarInfo monthCarInfo = monthCarInfoService.findById(id);
            model.addAttribute("monthCarInfo", monthCarInfo);
            List<FixedCarManager> fixedCars = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
            model.addAttribute("fixedCars", fixedCars);
        }
        return "/backend/monthcarinfo-edit";
    }

    @GetMapping("/monthcarinfo-payment.html")
    public String monthcarinfoPayment(Long id, Model model) {
        MonthCarInfo monthCarInfo = monthCarInfoService.findById(id);
        model.addAttribute("monthCarInfo", monthCarInfo);
        model.addAttribute("monthFee", monthCarInfoService.getMonthFee(monthCarInfo.getCarType()));
        return "/backend/monthcarinfo-payment";
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
    public void payment(@RequestParam Integer month,
                        @RequestParam Long id,
                        HttpServletRequest request) {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute(YstCommon.LOGIN_ACCOUNT);
        monthCarInfoService.payment(month, id, account);
    }
}
