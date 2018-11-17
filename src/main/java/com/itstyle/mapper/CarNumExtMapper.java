package com.itstyle.mapper;

import com.itstyle.domain.car.manager.enums.CarNumExtVo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarNumExtMapper extends JpaRepository<CarNumExtVo, Long>{
    CarNumExtVo findByPath(String path);
}
