package com.itstyle.service;

import com.itstyle.domain.car.manager.CarInfo;
import com.itstyle.domain.car.manager.MonthCarInfo;
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
    private CarInfoService carInfoService;

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
}
