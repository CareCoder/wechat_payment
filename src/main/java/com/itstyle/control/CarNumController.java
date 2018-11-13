package com.itstyle.control;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
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

@Controller
@RequestMapping("/carnum")
public class CarNumController {
    @Resource
    private CarNumService carNumService;

    @RequestMapping("/upload")
    @ResponseBody
    public Response upload(@RequestParam("file") MultipartFile file,
                       @RequestParam String carNum,
                       @RequestParam CarNumType carNumType) {
        int status = Status.NORMAL;
        try {
            status = carNumService.upload(file, carNum, carNumType);
        } catch (Exception e) {
            return Response.build(status, "系统错误", null);
        }
        return Response.build(status, "", null);
    }

    @RequestMapping("/download")
    @ResponseBody
    public ResponseEntity<byte[]> download(@RequestParam String carNum,
                                           @RequestParam CarNumType carNumType) {
        return carNumService.findByCarNumAndType(carNum, carNumType);
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public Response delete(@PathVariable("id") Long id) {
        try {
            carNumService.delete(id);
        } catch (Exception e) {
            return Response.build(Status.ERROR, "系统错误", null);
        }
        return Response.build(Status.NORMAL, "", null);
    }

    @RequestMapping("/query")
    @ResponseBody
    public Response query(String carNum, CarNumType carNumType) {
        CarNumVo vo;
        try {
            vo = carNumService.query(carNum, carNumType);
        } catch (Exception e) {
            return Response.build(Status.ERROR, "系统错误", null);
        }
        return Response.build(Status.NORMAL, "", new Gson().toJsonTree(vo));
    }
}
