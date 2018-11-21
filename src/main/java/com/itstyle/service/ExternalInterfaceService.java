package com.itstyle.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itstyle.common.YstCommon;
import com.itstyle.dao.RedisDao;
import com.itstyle.domain.car.manager.CarInfo;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.vo.Incrementmonly.response.IncrementMonly;
import com.itstyle.vo.Incrementmonly.response.MonlyCarAddInfo;
import com.itstyle.vo.Incrementmonly.response.MonlyCarRenewInfo;
import com.itstyle.vo.inition.response.Inition;
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

    public Inition inition() {
        Inition inition = new Inition();

        inition.synCarInfo = synCarInfo();
        return inition;
    }

    /**
     * 获取到临时的新增信息并且删除掉
     */
    public IncrementMonly incrementMonly() {
        List<MonlyCarAddInfo> mcais = gson.fromJson(redisDao.get(YstCommon.MONLY_CAR_ADD_INFO),
                new TypeToken<List<MonlyCarAddInfo>>(){}.getType());
        List<MonlyCarRenewInfo> mcri = gson.fromJson(redisDao.get(YstCommon.MONLY_CAR_RENEW_INFO),
                new TypeToken<List<MonlyCarRenewInfo>>() {
        }.getType());
        redisDao.delete(YstCommon.MONLY_CAR_ADD_INFO);
        redisDao.delete(YstCommon.MONLY_CAR_RENEW_INFO);
        IncrementMonly incrementMonly = new IncrementMonly();
        incrementMonly.monlyCarAddInfos = mcais;
        incrementMonly.monlyCarRenewInfos = mcri;
        return incrementMonly;
    }
}
