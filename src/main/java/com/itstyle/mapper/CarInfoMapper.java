package com.itstyle.mapper;

import com.itstyle.domain.car.manager.CarInfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CarInfoMapper extends JpaRepository<CarInfo, Long> {
    CarInfo findByCarNum(String carNum);
}
