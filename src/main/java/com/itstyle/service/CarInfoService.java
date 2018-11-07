package com.itstyle.service;

import com.itstyle.domain.carinfo.CarInfo;
import com.itstyle.mapper.CarInfoMapper;
import com.itstyle.utils.BeanUtilIgnore;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service
public class CarInfoService {
    @Resource
    private CarInfoMapper carInfoMapper;

    public List<CarInfo> list() {
        return carInfoMapper.findAll();
    }

    public CarInfo getById(Long id) {
        return carInfoMapper.findOne(id);
    }

    public CarInfo getByCarNum(String carNum) {
        return carInfoMapper.getByCarNum(carNum);
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
