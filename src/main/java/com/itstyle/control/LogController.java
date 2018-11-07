package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.log.SysLogger;
import com.itstyle.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequestMapping("/log")
@Controller
public class LogController {

    private LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/list")
    @ResponseBody
    public PageResponse<SysLogger> list(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                        @RequestParam(value = "size", required = false, defaultValue = "20") int limit) {
        PageResponse<SysLogger> pageResponse = logService.list(page, limit);
        log.info("[LogController] data is [{}]", pageResponse.getData());
        return pageResponse;
    }
}
