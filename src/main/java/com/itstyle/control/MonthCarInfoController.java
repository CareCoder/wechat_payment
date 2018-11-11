package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.service.MonthCarInfoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/monthcarinfo")
public class MonthCarInfoController {
    @Resource
    private MonthCarInfoService monthCarInfoService;

    @GetMapping("list")
    @ResponseBody
    public PageResponse<MonthCarInfo> list(int page, int limit, String query) {
        return monthCarInfoService.queryLimit(page, limit, query);
    }

}
