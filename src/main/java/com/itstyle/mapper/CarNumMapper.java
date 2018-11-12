package com.itstyle.mapper;

import com.itstyle.domain.car.manager.CarNumVo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarNumMapper extends JpaRepository<CarNumVo, Long> {
    CarNumVo findByCarNum1Type(String s);
}
