package com.itstyle.mapper;

import com.itstyle.domain.car.manager.CarInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface CarInfoMapper extends JpaRepository<CarInfo, Long>, JpaSpecificationExecutor<CarInfo> {
    CarInfo findByCarNum(String carNum);

    List<CarInfo> findByIsFreeIs(boolean b);

    List<CarInfo> findByIsBlackListIs(boolean b);
}
