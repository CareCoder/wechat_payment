package com.itstyle.service;

import com.itstyle.domain.carinfo.CarInfo;
import com.itstyle.mapper.CarInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarInfoService {
    @Resource
    private CarInfoMapper carInfoMapper;

    public List<CarInfo> list() {
        return carInfoMapper.getAll();
    }

    public CarInfo getById(Long id) {
        return carInfoMapper.getById(id);
    }

    public CarInfo getByCarNum(String carNum) {
        return carInfoMapper.getByCarNum(carNum);
    }

    public void save(CarInfo carInfo) {
        carInfoMapper.insert(carInfo);
    }

    public void delete(Long id) {
        carInfoMapper.delete(id);
    }

    public void update(CarInfo carInfo) {
        carInfoMapper.update(carInfo);
    }
}
