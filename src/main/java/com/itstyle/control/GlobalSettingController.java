package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.common.SystemLoggerHelper;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.FileResource.FileResourceBo;
import com.itstyle.domain.car.manager.BanListManager;
import com.itstyle.domain.car.manager.Fastigium;
import com.itstyle.domain.car.manager.FixedCarManager;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.caryard.ResponseAccessType;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.service.AccessTypeService;
import com.itstyle.service.FileResourceService;
import com.itstyle.service.GlobalSettingService;
import com.itstyle.utils.enums.Status;
import com.itstyle.vo.inition.response.ImageDisplay;
import com.itstyle.vo.inition.response.ImageDownloadUrl;
import com.itstyle.vo.inition.response.TextDisplay;
import com.itstyle.vo.phonenumber.response.PhoneNumber;
import com.itstyle.vo.phonenumber.response.PhoneNumberList;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/gs")
public class GlobalSettingController {
    @Resource
    private GlobalSettingService globalSettingService;
    @Resource
    private AccessTypeService accessTypeService;
    @Resource
    private FileResourceService fileResourceService;

    @GetMapping("/get/fastigium")
    public String fastigiumGet(Model model) {
        List<ResponseAccessType> accessTypes1 = accessTypeService.listNoPage();
        List<ResponseAccessType> accessTypes2 = accessTypeService.listNoPage();
        //业务需求这里只需要入口通道
        accessTypes1 = accessTypes1.stream().filter(e -> e.getChannelTypeId() % 2 == 1).collect(Collectors.toList());
        model.addAttribute("accessTypes1", accessTypes1);
        //业务需求这里只需要出口通道
        accessTypes2 = accessTypes2.stream().filter(e -> e.getChannelTypeId() % 4 == 2).collect(Collectors.toList());
        model.addAttribute("accessTypes2", accessTypes2);
        model.addAttribute("fastigium", globalSettingService.get(YstCommon.FASTIGIUM_KEY, Fastigium.class));
        return "/backend/fastigium";
    }

    @PostMapping("/set/fastigium")
    @ResponseBody
    public void fastigiumSet(Fastigium param) {
        globalSettingService.set(YstCommon.FASTIGIUM_KEY, param);
        SystemLoggerHelper.log("配置", "配置高峰期管理");
    }

    @GetMapping("/get/banlist")
    public String banlistSet(Model model) {
        model.addAttribute("banlist", globalSettingService.get(YstCommon.BANLISTMANAGER_KEY, BanListManager.class));
        return "/backend/banlist";
    }

    @PostMapping("/set/banlist")
    @ResponseBody
    public void banlistGet(BanListManager param) {
        globalSettingService.set(YstCommon.BANLISTMANAGER_KEY, param);
        SystemLoggerHelper.log("配置", "配置禁入车辆");
    }


    /***********************fixedcar**************************/

    @GetMapping("/get/fixedcar")
    public String fixedcarGet() {
        return "/backend/fixedcar";
    }

    @GetMapping("/edit/fixedcar")
    public String fixedcarGet(CarType carType, Model model) {
        List<FixedCarManager> f = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        AtomicReference<FixedCarManager> fixedCarManager = new AtomicReference<>();
        f.forEach(e -> {
            if (e.getCarType() == carType) {
                fixedCarManager.set(e);
            }
        });
        model.addAttribute("fixedCarManager", fixedCarManager.get());
        return "/backend/fixedcar-edit";
    }

    @PostMapping("/set/fixedcar")
    @ResponseBody
    public void fixedcarGet(FixedCarManager param) {
        List<FixedCarManager> f = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        for (int i = 0; i < f.size(); i++) {
            if (f.get(i).getCarType() == param.getCarType()) {
                f.set(i, param);
            }
        }
        globalSettingService.set(YstCommon.FIXEDCARMANAGER_KEY, f);
        SystemLoggerHelper.log("更新", "更新固定车辆名称");
    }

    @GetMapping("/get/fixedcar/data")
    @ResponseBody
    public PageResponse<FixedCarManager> fixedcarGetData() {
        List<FixedCarManager> f = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        //如果初次启动系统没有数据则生成默认数据并返回
        if (f == null) {
            f = FixedCarManager.defaultList();
            globalSettingService.set(YstCommon.FIXEDCARMANAGER_KEY, f);
        }
        return new PageResponse<>(0, "", (long) f.size(), f);
    }

    @RequestMapping("/get/specialcar")
    public String specialcarGet(Model model) {
        String keyWords = (String) globalSettingService.get(YstCommon.SPECAL_CAR, String.class);
        model.addAttribute("keyWords", keyWords);
        return "/backend/special-car";
    }

    @RequestMapping("/set/specialcar")
    @ResponseBody
    public void specialcarSet(String keyWords) {
        globalSettingService.set(YstCommon.SPECAL_CAR, keyWords);
        SystemLoggerHelper.log("更新", "更新特殊车辆关键字");
    }

    @RequestMapping("/get/led-info")
    public String ledInfoGet(Model model) {
        TextDisplay textDisplay = (TextDisplay) globalSettingService.get(YstCommon.LED_INFO, TextDisplay.class);
        model.addAttribute("textDisplay", textDisplay);
        return "/backend/led-info";
    }

    @PostMapping("/set/led-info")
    @ResponseBody
    public void ledInfoSet(TextDisplay textDisplay) {
        globalSettingService.set(YstCommon.LED_INFO, textDisplay);
    }

    @RequestMapping("/get/lcd-info")
    public String lcdInfoGet(Model model) {
        ImageDisplay imageDisplay = (ImageDisplay) globalSettingService.get(YstCommon.LCD_INFO, ImageDisplay.class);
        model.addAttribute("imageDisplay", imageDisplay);
        return "/backend/lcd-info";
    }

    @RequestMapping("/get/lcd-info/data")
    @ResponseBody
    public ImageDisplay lcdInfoGetData(Model model) {
        return (ImageDisplay) globalSettingService.get(YstCommon.LCD_INFO, ImageDisplay.class);
    }
    
    private static final int lcdInfoSize = 5;

    @PostMapping("/set/lcd-info")
    @ResponseBody
    public Response lcdInfoSet(MultipartFile file, Integer index, Integer switchingtTime) {
        ImageDisplay imageDisplay = (ImageDisplay) globalSettingService.get(YstCommon.LCD_INFO, ImageDisplay.class);
        if (imageDisplay == null) {
            imageDisplay = new ImageDisplay();
        }
        imageDisplay.switchingtTime = switchingtTime;
        if (file != null) {
            if (imageDisplay.urlList == null) {
                imageDisplay.urlList = new ArrayList<>(lcdInfoSize);
            }
            List<ImageDownloadUrl> urlList = imageDisplay.urlList;
            for (int i = 0; i < lcdInfoSize; i++) {
                if (urlList.size() < i + 1) {
                    urlList.add(i, new ImageDownloadUrl());
                }else if (urlList.get(i) == null) {
                    urlList.add(i, new ImageDownloadUrl());
                }
            }
            String uuid = UUID.randomUUID().toString();
            fileResourceService.upload(file, uuid);
            FileResourceBo fileResourceBo = fileResourceService.getByUuid(uuid);
            urlList.remove(index.intValue());
            urlList.add(index, new ImageDownloadUrl(fileResourceBo.getUrl()));
        }
        globalSettingService.set(YstCommon.LCD_INFO, imageDisplay);
        return Response.build(Status.NORMAL, null, null);
    }

    @PostMapping("/delete/lcd-info")
    @ResponseBody
    public Response deleteLcdByIndex(String fileName,Integer index){
        ImageDisplay imageDisplay = (ImageDisplay) globalSettingService.get(YstCommon.LCD_INFO, ImageDisplay.class);
        if (imageDisplay == null) {
            imageDisplay = new ImageDisplay();
        }
        if (fileName != "") {
            if (imageDisplay.urlList == null) {
                imageDisplay.urlList = new ArrayList<>(lcdInfoSize);
            }
            List<ImageDownloadUrl> urlList = imageDisplay.urlList;

            fileResourceService.deleteByFileName(fileName);
            urlList.remove(index.intValue());
            globalSettingService.set(YstCommon.LCD_INFO, imageDisplay);
            return Response.build(Status.NORMAL, null, null);
        }else{
            return Response.build(Status.ERROR, "没有可删除的图片！", null);
        }
    }

    @RequestMapping("/get/phone-num")
    public String phoneNumGet() {
        return "/backend/phone-num";
    }

    @RequestMapping("/get/phone-num/data")
    @ResponseBody
    public PhoneNumberList phoneNumGetData() {
        return (PhoneNumberList) globalSettingService.get(YstCommon.PHONE_NUMBER, PhoneNumberList.class);
    }

    private static final int PHONE_NUM_SIZE = 5;

    @RequestMapping("/set/phone-num")
    @ResponseBody
    public void phoneNumSet(HttpServletRequest request) {
        PhoneNumberList phoneNumberList = new PhoneNumberList();
        for (int i = 0; i < PHONE_NUM_SIZE; i++) {
            String timeStart = request.getParameter("timeStart[" + i + "]");
            String timeEnd = request.getParameter("timeEnd[" + i + "]");
            String phoneNumber = request.getParameter("phoneNumber[" + i + "]");

            PhoneNumber p = new PhoneNumber();
            p.setTimeStart(timeStart);
            p.setTimeEnd(timeEnd);
            p.setPhoneNumber(phoneNumber);

            phoneNumberList.getPhoneNumberList().add(p);
        }

        globalSettingService.set(YstCommon.PHONE_NUMBER, phoneNumberList);
    }
}
