package com.itstyle.mapper;

import com.itstyle.domain.car.manager.CarInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface CarInfoMapper extends JpaRepository<CarInfo, Long>, JpaSpecificationExecutor<CarInfo> {
    CarInfo findByCarNum(String carNum);
}
