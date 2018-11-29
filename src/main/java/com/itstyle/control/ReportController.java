package com.itstyle.control;

import com.itstyle.domain.car.manager.enums.CarType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportController {

    @RequestMapping("/charge/temp/list")
    public String chargeTempList(Model model) {
        model.addAttribute("type", CarType.TEMP_CAR_A.toString());
        return "/backend/chargelist";
    }
    @RequestMapping("/charge/month/list")
    public String chargeMonthList(Model model) {
        model.addAttribute("type", CarType.MONTH_CAR_A.toString());
        return "/backend/chargelist";
    }
}
