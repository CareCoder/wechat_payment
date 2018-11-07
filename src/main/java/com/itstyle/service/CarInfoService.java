package com.itstyle.service;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.carinfo.CarInfo;
import com.itstyle.mapper.CarInfoMapper;
import com.itstyle.utils.BeanUtilIgnore;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.sql.Timestamp;

@Service
public class CarInfoService {
    @Resource
    private CarInfoMapper carInfoMapper;

    public PageResponse<CarInfo> list(int page, int limit, String type) {
        CarInfo carInfo = new CarInfo();
        if (type.equals("free")) {
            carInfo.setIsFree(true);
        }else if(type.equals("blacklist")){
            carInfo.setIsBlackList(true);
        }
        Example<CarInfo> exapmle = Example.of(carInfo);
        return PageResponse.build(carInfoMapper.findAll(exapmle,PageResponse.getPageRequest(page, limit)));
    }

    public CarInfo getById(Long id) {
        return carInfoMapper.findOne(id);
    }

    public CarInfo getByCarNum(String carNum) {
        return carInfoMapper.findByCarNum(carNum);
    }

    public void save(CarInfo carInfo) {
        carInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        carInfoMapper.save(carInfo);
    }

    public void delete(Long id) {
        carInfoMapper.delete(id);
    }

    public void update(CarInfo carInfo) {
        Assert.notNull(carInfo.getId(), "id is null");
        CarInfo one = carInfoMapper.findOne(carInfo.getId());
        BeanUtilIgnore.copyPropertiesIgnoreNull(carInfo, one);
        one.setModifyTime(new Timestamp(System.currentTimeMillis()));
        carInfoMapper.save(one);
    }
}
