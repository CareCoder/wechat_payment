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
}
