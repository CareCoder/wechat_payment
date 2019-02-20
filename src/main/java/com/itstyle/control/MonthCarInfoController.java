package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.common.SystemLoggerHelper;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.account.Account;
import com.itstyle.domain.car.manager.FixedCarManager;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.service.GlobalSettingService;
import com.itstyle.service.MonthCarInfoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
        }
        List<FixedCarManager> fixedCars = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        model.addAttribute("fixedCars", fixedCars);
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
        MonthCarInfo carInfo = monthCarInfoService.findById(id);
        monthCarInfoService.delete(id);
        SystemLoggerHelper.log("删除", "删除月租车:"+carInfo.getCarNum());
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
    public String edit(MonthCarInfo monthCarInfo, @RequestParam Integer month,
                       HttpServletRequest request, HttpServletResponse httpResponse) {
        String result = "";
        try {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute(YstCommon.LOGIN_ACCOUNT);
            result = monthCarInfoService.edit(monthCarInfo, month, account);
        } catch (DataIntegrityViolationException exception) {
            httpResponse.setStatus(503);
        }
        SystemLoggerHelper.log("更新", "更新月租车:" + monthCarInfo.getCarNum());
        return result;
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
        MonthCarInfo byId = monthCarInfoService.findById(id);
        SystemLoggerHelper.log("续费", "续费月租车:"+ byId.getCarNum() + "续费"+ month +"月");
    }

    /**
     * 导出excel
     */
    @RequestMapping("/exportExcel")
    public ResponseEntity<byte[]> exportExcel() {
        return monthCarInfoService.exportExcel();
    }

    /**
     * 导入
     * @param file
     * @return
     */
    @RequestMapping("/importExcel")
    @ResponseBody
    public Response importExcel(MultipartFile file, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute(YstCommon.LOGIN_ACCOUNT);
        monthCarInfoService.importExcel(file, account);
        return Response.build(200, "", null);
    }
}
