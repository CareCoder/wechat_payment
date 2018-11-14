package com.itstyle.mapper;

import com.itstyle.domain.car.manager.CarNumVo;
import com.itstyle.domain.car.manager.enums.CarNumType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarNumMapper extends JpaRepository<CarNumVo, Long> {
    CarNumVo findByCarNumAndCarNumTypeAndTime(String carNum, CarNumType carNumType, Long time);

    CarNumVo findByPath(String path);
}
