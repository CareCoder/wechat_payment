package com.itstyle.control;

import com.itstyle.common.YstCommon;
import com.itstyle.domain.car.manager.CarNumQueryVo;
import com.itstyle.domain.car.manager.CarNumVo;
import com.itstyle.domain.car.manager.FixedCarManager;
import com.itstyle.domain.car.manager.enums.CarNumExtVo;
import com.itstyle.domain.car.manager.enums.CarNumType;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.service.CarNumService;
import com.itstyle.service.GlobalSettingService;
import com.itstyle.utils.FeeUtil;
import com.itstyle.utils.enums.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/carnum")
public class CarNumController {
    @Resource
    private CarNumService carNumService;
    @Resource
    private GlobalSettingService globalSettingService;

    @GetMapping("/tempcarinfo.html")
    public String tempcarinfo(CarNumQueryVo queryVo, Model model) {
        if (queryVo.getPage() <= 0) {
            queryVo.setPage(1);
        }
        List<CarNumVo> carNumVos = carNumService.query(queryVo);
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
        return "/backend/tempcarinfo";
    }

    @GetMapping("/tempcarinfo-payment.html")
    public String tempcarinfo(Long id, Model model) {
        CarNumVo carNumVo = carNumService.findById(id);
        carNumVo.getCarNumExtVos().sort(Comparator.comparingInt(e1 -> e1.getCarNumType().ordinal()));
        long enterTime = 0;
        long leaveTime = 0;
        for (CarNumExtVo e : carNumVo.getCarNumExtVos()) {
            if (e.getCarNumType() == CarNumType.ENTER_BIG) {
                enterTime = e.getTime();
            } else if (e.getCarNumType() == CarNumType.LEAVE_BIG) {
                leaveTime = e.getTime();
            }
        }
        model.addAttribute("enterTime", enterTime);
        model.addAttribute("leaveTime", leaveTime);
        model.addAttribute("stopTime", FeeUtil.secondToTime(leaveTime - enterTime));
        model.addAttribute("userName", "");
        model.addAttribute("fee", FeeUtil.convert(carNumVo.getFee()));
        model.addAttribute("vo", carNumVo);
        return "/backend/tempcarinfo-payment";
    }

    @RequestMapping("/upload")
    @ResponseBody
    public Response upload(@RequestParam("file") MultipartFile file, CarNumVo carNumVo, CarNumExtVo carNumExtVo, Long leaveTime) {
        int status = Status.NORMAL;
        try {
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
}
