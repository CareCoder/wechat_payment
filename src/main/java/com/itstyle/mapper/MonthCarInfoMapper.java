package com.itstyle.mapper;

import com.itstyle.domain.car.manager.MonthCarInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface MonthCarInfoMapper extends JpaRepository<MonthCarInfo, Long>, JpaSpecificationExecutor<MonthCarInfo> {
}
