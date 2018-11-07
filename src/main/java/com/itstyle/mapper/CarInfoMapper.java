package com.itstyle.mapper;

import com.itstyle.domain.carinfo.CarInfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CarInfoMapper extends JpaRepository<CarInfo, Long> {
    CarInfo getByCarNum(String carNum);
}
