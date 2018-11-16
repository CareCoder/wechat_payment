package com.itstyle.control;

import com.itstyle.domain.car.manager.CarNumQueryVo;
import com.itstyle.domain.car.manager.CarNumVo;
import com.itstyle.domain.car.manager.enums.CarNumExtVo;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.service.CarNumService;
import com.itstyle.utils.enums.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/carnum")
public class CarNumController {
    @Resource
    private CarNumService carNumService;

    @GetMapping("/tempcarinfo.html")
    public String tempcarinfo(CarNumQueryVo queryVo, Model model) {
        List<CarNumVo> carNumVos = carNumService.query(queryVo);
        carNumVos.forEach(e -> {
            List<CarNumExtVo> carNumExtVos = e.getCarNumExtVos();
            carNumExtVos.sort(Comparator.comparingInt(e1 -> -e1.getCarNumType().ordinal()));
        });
        model.addAttribute("carNumVos", carNumVos);
        model.addAttribute("queryVo", queryVo);
        return "/backend/tempcarinfo";
    }

    @RequestMapping("/upload")
    @ResponseBody
    public Response upload(@RequestParam("file") MultipartFile file, CarNumVo carNumVo, CarNumExtVo carNumExtVo) {
        int status = Status.NORMAL;
        try {
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
