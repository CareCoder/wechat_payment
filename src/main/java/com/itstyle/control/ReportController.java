package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.car.manager.enums.ChargeType;
import com.itstyle.domain.report.ChargeRecord;
import com.itstyle.service.ChargeRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/report")
public class ReportController {
    @Resource
    private ChargeRecordService chargeRecordService;

    @RequestMapping("/list.html")
    public String reportList() {
        return "/backend/report-list";
    }

    @RequestMapping("/charge/temp/list.html")
    public String chargeTempList(Model model) {
        model.addAttribute("carType", CarType.TEMP_CAR_A.toString());
        return "/backend/chargelist-temp";
    }
    @RequestMapping("/charge/month/list.html")
    public String chargeMonthList(Model model) {
        model.addAttribute("carType", CarType.MONTH_CAR_A.toString());
        return "/backend/chargelist-month";
    }

    @RequestMapping("/charge/list")
    @ResponseBody
    public PageResponse<ChargeRecord> list(int page, int limit, ChargeType chargeType,CarType carType,
                                           String chargePersonnel, Long startTime, Long endTime) {
        return chargeRecordService.query(page, limit, chargeType, carType,
                chargePersonnel, startTime, endTime);
    }
}
