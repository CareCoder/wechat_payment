package com.itstyle.mapper;

import com.itstyle.domain.car.manager.MonthCarInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface MonthCarInfoMapper extends JpaRepository<MonthCarInfo, Long>, JpaSpecificationExecutor<MonthCarInfo> {
    /**
     * 根据车牌号获取指定车辆信息
     * @param carNum
     * @return
     */
    MonthCarInfo findByCarNum(String carNum);

    List<MonthCarInfo> findByIsMonth(boolean b);
}
