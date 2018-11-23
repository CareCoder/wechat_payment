package com.itstyle.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itstyle.common.YstCommon;
import com.itstyle.dao.RedisDao;
import com.itstyle.domain.car.manager.CarInfo;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.domain.caryard.CarYardName;
import com.itstyle.domain.caryard.ResponsePassCarStatus;
import com.itstyle.utils.BeanUtilIgnore;
import com.itstyle.vo.incrementmonly.response.IncrementMonly;
import com.itstyle.vo.incrementmonly.response.MonlyCarAddInfo;
import com.itstyle.vo.incrementmonly.response.MonlyCarRenewInfo;
import com.itstyle.vo.inition.response.AccessAuthoritySetup;
import com.itstyle.vo.inition.response.Inition;
import com.itstyle.vo.inition.response.ParkingSetup;
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

        inition.synCarInfo = synCarInfo();
        inition.carYardName = carYardName();
        inition.accessAuthoritySetup = getAccessAuthoritySetup();
        return inition;
    }

    public CarYardName carYardName() {
        CarYardName carYardName = (CarYardName) globalSettingService.get(YstCommon.CAR_YARD_NAME, CarYardName.class);
        return carYardName;
    }

    public List<AccessAuthoritySetup> getAccessAuthoritySetup() {
        List<ResponsePassCarStatus> list = passPermissionService.list();
        if (list != null) {
            return list.stream().map(responsePassCarStatus -> {
                AccessAuthoritySetup accessAuthoritySetup = new AccessAuthoritySetup();
                accessAuthoritySetup.setIp(responsePassCarStatus.getIp());
                accessAuthoritySetup.setName(responsePassCarStatus.getChannelName());
                accessAuthoritySetup.setType(responsePassCarStatus.getChannelTypeName());
                accessAuthoritySetup.setEntrance_tempCar_1(isAllow(responsePassCarStatus.getEntrance_tempCar_1()));
                accessAuthoritySetup.setEntrance_tempCar_2(isAllow(responsePassCarStatus.getEntrance_tempCar_2()));
                accessAuthoritySetup.setEntrance_monlyCar_1(isAllow(responsePassCarStatus.getEntrance_monlyCar_1()));
                accessAuthoritySetup.setEntrance_monlyCar_2(isAllow(responsePassCarStatus.getEntrance_monlyCar_2()));
                accessAuthoritySetup.setEntrance_specialCar_1(isAllow(responsePassCarStatus.getEntrance_specialCar_1()));
                accessAuthoritySetup.setEntrance_specialCar_2(isAllow(responsePassCarStatus.getEntrance_specialCar_2()));
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
        return index == 1 ? true : false;
    }
}
