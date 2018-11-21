package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.account.Role;
import com.itstyle.domain.log.SysLoggerResponse;
import com.itstyle.service.LogService;
import com.itstyle.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Slf4j
@RequestMapping("/log")
@Controller
public class LogController {

    private LogService logService;
    private RoleService roleService;

    @Autowired
    public LogController(LogService logService, RoleService roleService) {
        this.logService = logService;
        this.roleService = roleService;
    }

    @GetMapping("/list")
    @ResponseBody
    public PageResponse<SysLoggerResponse> list(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                @RequestParam(value = "limit", required = false, defaultValue = "20") int limit,
                                                Long start, Long end, Long roleId) {
        PageResponse<SysLoggerResponse> pageResponse = logService.list(page, limit, new Date(start),
                new Date(end), roleId);
        log.info("[LogController] data is [{}]", pageResponse.getData());
        return pageResponse;
    }

    @GetMapping("/page")
    public String getLogPage(Model model) {
        List<Role> list = roleService.list();
        if (list != null && list.size() > 0) {
            model.addAttribute("role", list.get(0));
        }
        return "/backend/log";
    }
}
