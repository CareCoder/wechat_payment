package com.itstyle.control;

import com.itstyle.domain.car.manager.CarNumVo;
import com.itstyle.domain.car.manager.enums.CarNumType;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.service.CarNumService;
import com.itstyle.utils.enums.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/carnum")
public class CarNumController {
    @Resource
    private CarNumService carNumService;

    @RequestMapping("/upload")
    @ResponseBody
    public Response upload(@RequestParam("file") MultipartFile file, CarNumVo carNumVo) {
        int status = Status.NORMAL;
        try {
            status = carNumService.upload(file, carNumVo);
        } catch (Exception e) {
            return Response.build(status, "系统错误", null);
        }
        return Response.build(status, "", null);
    }

    @RequestMapping("/download")
    @ResponseBody
    public ResponseEntity<byte[]> download(@RequestParam String path) {
        return carNumService.download(path);
    }

    @RequestMapping("/download2")
    @ResponseBody
    public ResponseEntity<byte[]> download2(@RequestParam String carNum,
                                            @RequestParam CarNumType carNumType,
                                            @RequestParam Long time) {
        return carNumService.download(carNum, carNumType, time);
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
