package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.car.manager.enums.ChargeType;
import com.itstyle.domain.report.ChargeRecord;
import com.itstyle.service.CarNumService;
import com.itstyle.service.ChargeRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {
    @Resource
    private ChargeRecordService chargeRecordService;
    @Resource
    private CarNumService carNumService;

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
                                           CarType carRealType,
                                           String chargePersonnel, Long startTime, Long endTime) {
        return chargeRecordService.query(page, limit, chargeType, carType,carRealType,
                chargePersonnel, startTime, endTime);
    }

    @RequestMapping("/statistics/temp")
    @ResponseBody
    public List<Object> statisticsTemp(Integer count) {
        return chargeRecordService.statisticsTemp(CarType.TEMP_CAR_A.ordinal(), count);
    }

    @RequestMapping("/statistics/month")
    @ResponseBody
    public List<Object> statisticsMonth(Integer count) {
        return chargeRecordService.statisticsTemp(CarType.MONTH_CAR_A.ordinal(),count);
    }

    @RequestMapping("/statistics/access")
    @ResponseBody
    public List<Object> statisticsAccess(Integer count) {
        return carNumService.statisticsAccess(count);
    }

    /**
     * 导出收费明细的excel
     */
    @RequestMapping("/charge/exportExcel")
    public ResponseEntity<byte[]> exportExcel(CarType carType) {
        return chargeRecordService.exportExcel(carType);
    }
}
