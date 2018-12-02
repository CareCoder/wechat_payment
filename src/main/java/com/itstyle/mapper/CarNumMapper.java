package com.itstyle.mapper;

import com.itstyle.domain.car.manager.CarNumVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarNumMapper extends JpaRepository<CarNumVo, Long>, JpaSpecificationExecutor<CarNumVo> {
    @Query(value = "select COUNT(1) AS total FROM car_num as cn GROUP BY DAY(from_unixtime(cn.time/1000)) ORDER BY cn.time desc LIMIT 0 , ?1", nativeQuery = true)
    List<Object> statisticsAccess(Integer count);
}
