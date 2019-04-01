package com.itstyle.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.itstyle.common.YstCommon;
import com.itstyle.dao.RedisDao;
import com.itstyle.domain.car.manager.CarInfo;
import com.itstyle.domain.car.manager.FastigiumList;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.domain.car.manager.enums.CarType2;
import com.itstyle.domain.caryard.CarYardName;
import com.itstyle.domain.caryard.EquipmentStatus;
import com.itstyle.domain.caryard.ResponsePassCarStatus;
import com.itstyle.domain.report.DeleteRecord;
import com.itstyle.task.AssessTokenTask;
import com.itstyle.utils.HttpUtils;
import com.itstyle.vo.deletevehicleinfo.response.DeleteInfo;
import com.itstyle.vo.deletevehicleinfo.response.DeleteVehicleInfo;
import com.itstyle.vo.incrementmonly.response.IncrementMonly;
import com.itstyle.vo.incrementmonly.response.MonlyCarAddInfo;
import com.itstyle.vo.incrementmonly.response.MonlyCarRenewInfo;
import com.itstyle.vo.inition.response.*;
import com.itstyle.vo.phonenumber.response.PhoneNumber;
import com.itstyle.vo.phonenumber.response.PhoneNumberList;
import com.itstyle.vo.syncarinfo.response.BlackListVehicle;
import com.itstyle.vo.syncarinfo.response.FreeVehicle;
import com.itstyle.vo.syncarinfo.response.MonlyCarInfo;
import com.itstyle.vo.syncarinfo.response.SynCarInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExternalInterfaceService {
    @Resource
    private RedisDao redisDao;

    @Resource
    private Gson gson;

    @Resource
    private MonthCarInfoService monthCarInfoService;

    @Resource
    private GlobalSettingService globalSettingService;

    @Resource
    private CarInfoService carInfoService;

    @Resource
    private PassPermissionService passPermissionService;

    @Resource
    private DeleteRecordService deleteRecordService;

    @Resource
    private AssessTokenTask assessTokenTask;

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
        FastigiumList fastigiumList = (FastigiumList) globalSettingService.get(YstCommon.FASTIGIUM_KEY, FastigiumList.class);
        String keyWords = (String) globalSettingService.get(YstCommon.SPECAL_CAR, String.class);
        if(fastigiumList!=null){
            vehicleManagement.setPeakVehicle(fastigiumList.getFastigiumList());
        }else{
            fastigiumList = new FastigiumList();
            vehicleManagement.setPeakVehicle(fastigiumList.getFastigiumList());
        }
        vehicleManagement.specialCar = keyWords;
        return vehicleManagement;
    }


    private CarYardNameResp carYardName() {
        CarYardName carYardName = (CarYardName) globalSettingService.get(YstCommon.CAR_YARD_NAME, CarYardName.class);
        //获取剩余车位数,如果未获取到则默认为车场总数
        Integer remainingParkingNum = (Integer) globalSettingService.get(YstCommon.REMAINING_PARKING_NUM, Integer.class);
        Integer totleParkingNum = carYardName.getParkingNum();
        if (remainingParkingNum != null) {
            //如果获取到的剩余车位数大于等于总床位数，则让其等于总车位数
            if(remainingParkingNum >= totleParkingNum){
                remainingParkingNum = totleParkingNum;
                globalSettingService.set(YstCommon.REMAINING_PARKING_NUM,remainingParkingNum);
            }else if(remainingParkingNum <= 0){
                //如果获取到的剩余车位数小于等于0，则让其等于0
                remainingParkingNum = 0;
                globalSettingService.set(YstCommon.REMAINING_PARKING_NUM,remainingParkingNum);
            }
            carYardName.setParkingNum(remainingParkingNum);
        }
        CarYardNameResp resp = CarYardNameResp.build(carYardName);
        resp.totalParkingNum = totleParkingNum;
        return resp;
    }

    private List<AccessAuthoritySetup> getAccessAuthoritySetup() {
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
        List<CarInfo> carInfos = carInfoService.createTimeQuery(startTime, endTime);
        IncrementMonly icm = new IncrementMonly();
        icm.monlyCarAddInfos = carAddInfo.stream().map(MonlyCarAddInfo::convert).collect(Collectors.toList());
        icm.monlyCarRenewInfos = carRenewInfo.stream().map(MonlyCarRenewInfo::convert).collect(Collectors.toList());
        icm.blackVehicleAdd = carInfos.stream().filter(e -> e.getIsBlackList() != null && e.getIsBlackList()).map(BlackListVehicle::convert).collect(Collectors.toList());
        icm.freeVehicleAdd = carInfos.stream().filter(e -> e.getIsFree() != null && e.getIsFree()).map(FreeVehicle::convert).collect(Collectors.toList());
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
        PhoneNumberList phoneNumberList = (PhoneNumberList) globalSettingService.get(YstCommon.PHONE_NUMBER, PhoneNumberList.class);
        if (phoneNumberList != null) {
            List<PhoneNumber> tempList = phoneNumberList.getPhoneNumberList();
            if (tempList != null) {
                List<PhoneNumber> collect = tempList.stream().filter(e -> StringUtils.isNotEmpty(e.getPhoneNumber())).collect(Collectors.toList());
                phoneNumberList.setPhoneNumberList(collect);
            }
        }
        return phoneNumberList;
    }

    /**
     * 获取剩余车位数
     */
    public Integer restParkNum() {
        CarYardNameResp carYardName = carYardName();
        if (carYardName != null) {
            return carYardName.parkingNum;
        }
        return 0;
    }

    /**
     * 获取删除的月租车和黑名单车辆信息
     */
    public DeleteVehicleInfo fetchDeleteVehicleInfo(Long startTime, Long endTime) {
        DeleteVehicleInfo deleteVehicleInfo = new DeleteVehicleInfo();

        List<DeleteRecord> deleteRecords = deleteRecordService.query(startTime, endTime);

        List<DeleteInfo> blackListDeleteInfos = deleteRecords.stream()
                .filter(e -> e.getCarType2() == CarType2.BLACK_LIST_CAR)
                .map(e -> new DeleteInfo(e.getCarNum()))
                .collect(Collectors.toList());
        List<DeleteInfo> monlyCarDeleteInfos = deleteRecords.stream()
                .filter(e -> e.getCarType2() == CarType2.MONTH_CAR)
                .map(e -> new DeleteInfo(e.getCarNum()))
                .collect(Collectors.toList());
        deleteVehicleInfo.blackListDeleteInfos = blackListDeleteInfos;
        deleteVehicleInfo.monlyCarDeleteInfos = monlyCarDeleteInfos;

        return deleteVehicleInfo;
    }

    /**
     * 定时把外设的信息上传给服务器做更新
     */
    public void uploadEquipmentStatus(EquipmentStatus equipmentStatus) {
        log.info("uploadEquipmentStatus equipmentStatus = {}",gson.toJson(equipmentStatus));
        redisDao.hset(YstCommon.EQUIPMENT_STATUS, equipmentStatus.getPassWayName(), gson.toJson(equipmentStatus));
    }

    /**
     * 微信二维码
     */
    public String createTemporaryQRCode(Integer args){
        try {
            String json = "{\"expire_seconds\": 86400,\"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": \""+ args +"\"}}}";
            String strResult = HttpUtils.HttPost(
                    "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + assessTokenTask.getAssessToken(),
                    json);
            JSONObject jsonObject = JSON.parseObject(strResult);
            //String ticket = (String) jsonObject.get("ticket");
            log.info("strResult:"+strResult);
            String url = (String) jsonObject.get("url");
            log.info("url:"+url);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
