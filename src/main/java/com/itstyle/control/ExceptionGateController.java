package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.exceptiongate.ExceptionGate;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.service.ExceptionGateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Slf4j
@Controller
@RequestMapping("/exception_gate")
public class ExceptionGateController {
    @Resource
    private ExceptionGateService exceptionGateService;

    @RequestMapping("/upload")
    @ResponseBody
    public Response upload(@RequestParam("file") MultipartFile file, ExceptionGate exceptionGate) {
        int status = exceptionGateService.upload(file, exceptionGate);
        return Response.build(status, "", null);
    }

    @RequestMapping("/list.html")
    public String listHtml() {
        return "/backend/exception-gate-list";
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageResponse<ExceptionGate> list(Integer page, Integer limit, Long startTime, Long endTime) {
        return PageResponse.build(exceptionGateService.query(page, limit, startTime, endTime));
    }

    @RequestMapping("/watch-car-img")
    public String watchCarImg(Long id, Model model) {
        ExceptionGate exceptionGate = exceptionGateService.findById(id);
        model.addAttribute("img" + 0, exceptionGate.getOperImgUuid());
        return "/backend/watch-car-img";
    }
}
