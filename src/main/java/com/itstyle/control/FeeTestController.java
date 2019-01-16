package com.itstyle.control;

import com.itstyle.domain.park.resp.Response;
import com.itstyle.service.FeeTestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/fee_test")
public class FeeTestController {
    @Resource
    private FeeTestService feeTestService;

    @GetMapping("/page")
    public String page() {
        return "/backend/fee-test";
    }

    @GetMapping("/get")
    @ResponseBody
    public Response get(Integer chargingStandard, Integer carType, Long start, Long end) {
        if (chargingStandard == 1) {
            return feeTestService.byCharges(carType, start, end);
        }
        if (chargingStandard == 2) {
            return feeTestService.standardCharges(carType, start, end);
        }
        if (chargingStandard == 3) {
            return feeTestService.szCharges(carType, start, end);
        }
        return null;
    }
}
