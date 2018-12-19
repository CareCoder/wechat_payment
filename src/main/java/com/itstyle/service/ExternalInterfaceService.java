package com.itstyle.service;

import com.google.gson.Gson;
import com.itstyle.common.YstCommon;
import com.itstyle.dao.RedisDao;
import com.itstyle.domain.car.manager.CarInfo;
import com.itstyle.domain.car.manager.Fastigium;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.domain.caryard.CarYardName;
import com.itstyle.domain.caryard.ResponsePassCarStatus;
import com.itstyle.vo.incrementmonly.response.IncrementMonly;
import com.itstyle.vo.incrementmonly.response.MonlyCarAddInfo;
import com.itstyle.vo.incrementmonly.response.MonlyCarRenewInfo;
import com.itstyle.vo.inition.response.*;
import com.itstyle.vo.phonenumber.response.PhoneNumberList;
import com.itstyle.vo.syncarinfo.response.BlackListVehicle;
import com.itstyle.vo.syncarinfo.response.FreeVehicle;
import com.itstyle.vo.syncarinfo.response.MonlyCarInfo;
import com.itstyle.vo.syncarinfo.response.SynCarInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExternalInterfaceService {
    @Resource
    private MonthCarInfoService monthCarInfoService;
    @Resource
    private RedisDao redisDao;
    @Resource
    private Gson gson;
    @Resource
    private GlobalSettingService globalSettingService;

    @Resource
    private CarInfoService carInfoService;

    @Resource
    private PassPermissionService passPermissionService;

    public SynCarInfo synCarInfo() {
        SynCarInfo synCarInfo = new SynCarInfo();
        List<MonthCarInfo> mcList = monthCarInfoService.list();
        synCarInfo.monlyCarInfo = mcList.stream().map(MonlyCarInfo::convert).collect(Collectors.toList());
        List<CarInfo> blackList = carInfoService.getBlackList();
        synCarInfo.blackListVehicle = blackList.stream().map(BlackListVehicle::convert).collect(Collectors.toList());
        List<CarInfo> freeList = carInfoService.getFree();
        synCarInfo.freeVehicle = freeList.stream().map(FreeVehicle::convert).collect(Collectors.toList());
        return synCarInfo;
    }

    public Inition inition() {
        Inition inition = new Inition();

        inition.vehicleManagement = getVehicleManagement();
        inition.carYardName = carYardName();
        inition.accessAuthoritySetup = getAccessAuthoritySetup();
        inition.imageDisplay = getImageDisplay();
        inition.textDisplay = getTextDisplay();
        return inition;
    }

    /**
     * 获取针对LED文字信息发布，界面参数新增功能文档
     */
    private TextDisplay getTextDisplay() {
        return (TextDisplay) globalSettingService.get(YstCommon.LED_INFO, TextDisplay.class);
    }

    /**
     * 获取针对LCD广告图片发布，界面参考新增功能文档
     */
    private ImageDisplay getImageDisplay() {
        ImageDisplay imageDisplay = (ImageDisplay) globalSettingService.get(YstCommon.LCD_INFO, ImageDisplay.class);
        if (imageDisplay != null && imageDisplay.urlList != null) {
            imageDisplay.urlList = imageDisplay.urlList.stream().filter(e -> e != null && e.imageDownloadUrl != null).collect(Collectors.toList());
        }
        return imageDisplay;
    }

    private VehicleManagement getVehicleManagement() {
        VehicleManagement vehicleManagement = new VehicleManagement();
        List<CarInfo> blackList = carInfoService.getBlackList();
        vehicleManagement.blackVehicle = blackList.stream().map(BlackListVehicle::convert).collect(Collectors.toList());
        List<CarInfo> freeList = carInfoService.getFree();
        vehicleManagement.freeVehicle = freeList.stream().map(FreeVehicle::convert).collect(Collectors.toList());
        Fastigium Fastigium = (Fastigium) globalSettingService.get(YstCommon.FASTIGIUM_KEY, Fastigium.class);
        ForbidenVehicle fv = new ForbidenVehicle();
        String keyWords = (String) globalSettingService.get(YstCommon.SPECAL_CAR, String.class);
        fv.setFastigium(Fastigium);
        fv.specialCar = keyWords;
        vehicleManagement.forbidenVehicle = fv;
        return vehicleManagement;
    }

    public CarYardName carYardName() {
        CarYardName carYardName = (CarYardName) globalSettingService.get(YstCommon.CAR_YARD_NAME, CarYardName.class);
        //获取剩余车位数,如果未获取到则默认为车场总数
        Integer remainingParkingNum = (Integer) globalSettingService.get(YstCommon.REMAINING_PARKING_NUM, Integer.class);
        if (remainingParkingNum != null && carYardName != null) {
            carYardName.setParkingNum(remainingParkingNum);
        }
        return carYardName;
    }

    public List<AccessAuthoritySetup> getAccessAuthoritySetup() {
        List<ResponsePassCarStatus> list = passPermissionService.list();
        if (list != null) {
            return list.stream().map(responsePassCarStatus -> {
                AccessAuthoritySetup accessAuthoritySetup = new AccessAuthoritySetup();
                accessAuthoritySetup.setIp(responsePassCarStatus.getIp());
                accessAuthoritySetup.setCameraIp(responsePassCarStatus.getCamera());
                accessAuthoritySetup.setName(responsePassCarStatus.getChannelName());
                accessAuthoritySetup.setType(responsePassCarStatus.getChannelTypeName());
                accessAuthoritySetup.setBlueCar(isAllow(responsePassCarStatus.getBlueCar()));
                accessAuthoritySetup.setYellowCar(isAllow(responsePassCarStatus.getYellowCar()));
                accessAuthoritySetup.setGreenCar(isAllow(responsePassCarStatus.getGreenCar()));
                accessAuthoritySetup.setBlackCar(isAllow(responsePassCarStatus.getBlackCar()));
                accessAuthoritySetup.setMonlyCar_A(isAllow(responsePassCarStatus.getMonlyCar_A()));
                accessAuthoritySetup.setMonlyCar_B(isAllow(responsePassCarStatus.getMonlyCar_B()));
                accessAuthoritySetup.setMonlyCar_C(isAllow(responsePassCarStatus.getMonlyCar_C()));
                accessAuthoritySetup.setSpecialCar(isAllow(responsePassCarStatus.getSpecialCar()));
                return accessAuthoritySetup;
            }).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 获取到临时的新增信息并且删除掉
     */
    public IncrementMonly incrementMonly(Long startTime, Long endTime) {
        List<MonthCarInfo> carAddInfo = monthCarInfoService.getCarAddInfo(startTime, endTime);
        List<MonthCarInfo> carRenewInfo = monthCarInfoService.getCarRenewInfo(startTime, endTime);
        IncrementMonly icm = new IncrementMonly();
        icm.monlyCarAddInfos = carAddInfo.stream().map(MonlyCarAddInfo::convert).collect(Collectors.toList());
        icm.monlyCarRenewInfos = carRenewInfo.stream().map(MonlyCarRenewInfo::convert).collect(Collectors.toList());
        return icm;
    }

    private boolean isAllow(Integer index) {
        if (index == null) {
            return false;
        }
        return index == 1;
    }

    /**
     * 网络呼叫号码获取
     */
    public PhoneNumberList fetchPhoneNumber() {
        return (PhoneNumberList) globalSettingService.get(YstCommon.PHONE_NUMBER, PhoneNumberList.class);
    }
}
