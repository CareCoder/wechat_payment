package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.account.Account;
import com.itstyle.domain.car.manager.CarNumQueryVo;
import com.itstyle.domain.car.manager.CarNumVo;
import com.itstyle.domain.car.manager.FixedCarManager;
import com.itstyle.domain.car.manager.enums.CarNumExtVo;
import com.itstyle.domain.caryard.ResponseAccessType;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.service.AccessTypeService;
import com.itstyle.service.CarNumService;
import com.itstyle.service.GlobalSettingService;
import com.itstyle.utils.FeeUtil;
import com.itstyle.utils.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/carnum")
public class CarNumController {
    @Resource
    private CarNumService carNumService;
    @Resource
    private GlobalSettingService globalSettingService;
    @Resource
    private AccessTypeService accessTypeService;

    @GetMapping("/tempcarinfo.html")
    public String tempcarinfo(CarNumQueryVo queryVo, Model model) {
        if (queryVo.getPage() <= 0) {
            queryVo.setPage(1);
        }
        Page<CarNumVo> page = carNumService.query(queryVo, null);
        List<CarNumVo> carNumVos = page.getContent();
//                .stream()
//                .filter(e -> e.getCarNumExtVos() != null && e.getCarNumExtVos().size() > 1)
//                .collect(Collectors.toList());
        carNumVos.forEach(e -> {
            List<CarNumExtVo> carNumExtVos = e.getCarNumExtVos();
            carNumExtVos.sort(Comparator.comparingInt(e1 -> e1.getCarNumType().ordinal()));
        });
        model.addAttribute("carNumVos", carNumVos);
        model.addAttribute("queryVo", queryVo);
        List<FixedCarManager> fixedCars = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        model.addAttribute("fixedCars", fixedCars);

        model.addAttribute("maxPage", Math.ceil(page.getTotalElements() / 4));
        return "/backend/tempcarinfo";
    }

    @RequestMapping("/access/report.html")
    public String accessReport(Model model) {
        List<FixedCarManager> fixedCars = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        model.addAttribute("fixedCars", fixedCars);
        List<ResponseAccessType> accessTypes = accessTypeService.listNoPage();
        model.addAttribute("accessTypes", accessTypes);
        return "/backend/access-report";
    }
    @GetMapping("/tempcarinfo-payment.html")
    public String tempcarinfo(Long id, Model model, HttpSession session) {
        CarNumVo carNumVo = carNumService.findById(id);
        carNumVo.getCarNumExtVos().sort(Comparator.comparingInt(e1 -> e1.getCarNumType().ordinal()));
        Long enterTime = carNumVo.getTime();
        Long leaveTime = carNumVo.getLTime();
        Account account = (Account)session.getAttribute(YstCommon.LOGIN_ACCOUNT);
        String userName = "";
        if (account != null) {
            userName = account.getUsername();
        }
        model.addAttribute("id", id);
        model.addAttribute("enterTime", enterTime);
        model.addAttribute("leaveTime", leaveTime);
        model.addAttribute("stopTime", leaveTime == null ? "" : FeeUtil.secondToTime(leaveTime - enterTime));
        model.addAttribute("userName", userName);
        model.addAttribute("fee", carNumVo.getFee() == null ? "" :FeeUtil.convert(carNumVo.getFee()));
        model.addAttribute("vo", carNumVo);
        for (int i = 0; i < carNumVo.getCarNumExtVos().size(); i++) {
            model.addAttribute("img"+i, carNumVo.getCarNumExtVos().get(i).getUuid());
        }
        return "/backend/tempcarinfo-payment";
    }

    @PostMapping("/tempcarinfo-payment/confirm")
    @ResponseBody
    public String tempcarinfoPaymentConfirm(Long id, HttpSession session) {
        log.info("收费放行 id = {}", id);
        Account account = (Account) session.getAttribute(YstCommon.LOGIN_ACCOUNT);
        return carNumService.tempcarinfoPaymentConfirm(id, account);
    }

    @RequestMapping("/upload")
    @ResponseBody
    public Response upload(@RequestParam("file") MultipartFile file, CarNumVo carNumVo, CarNumExtVo carNumExtVo, Long leaveTime, HttpServletRequest request) {
        int status = Status.NORMAL;
        try {
            carNumVo.setLTime(leaveTime);
            carNumExtVo.setTime(leaveTime);//这里不要mvc自动注入 是因为两个注入对象的param相同了
            status = carNumService.upload(file, carNumVo, carNumExtVo);
        } catch (Exception e) {
            return Response.build(status, "系统错误", null);
        }
        return Response.build(status, "", null);
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<byte[]> download(@RequestParam String path) {
        return carNumService.download(path);
    }

    @RequestMapping("/download2")
    @ResponseBody
    public ResponseEntity<byte[]> download2(CarNumVo carNumVo, CarNumExtVo carNumExtVo) {
        return carNumService.download(carNumVo, carNumExtVo);
    }

    @RequestMapping("/delete/{path}")
    @ResponseBody
    public Response delete(@PathVariable("path") String path) {
        try {
            carNumService.delete(path);
        } catch (Exception e) {
            return Response.build(Status.ERROR, "系统错误", null);
        }
        return Response.build(Status.NORMAL, "", null);
    }

    @RequestMapping("/query")
    @ResponseBody
    public Response query(CarNumVo carNumVo) {
        List<CarNumVo> vos;
        try {
            vos = carNumService.query(carNumVo);
        } catch (Exception e) {
            return Response.build(Status.ERROR, "系统错误", null);
        }
        return Response.build(Status.NORMAL, "", vos);
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageResponse query(CarNumQueryVo queryVo, String isEnter) {
        if (StringUtils.isEmpty(isEnter)) {
            queryVo.setStartTime(null);
            queryVo.setEndTime(null);
        }else{
            queryVo.setLeaveStartTime(null);
            queryVo.setLeaveEndTime(null);
        }
        Page<CarNumVo> page = carNumService.query(queryVo, isEnter);
        return PageResponse.build(page);
    }
}
