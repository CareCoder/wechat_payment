package com.itstyle.mapper;

import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.report.ChargeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChargeRecordMapper extends JpaRepository<ChargeRecord, Long>, JpaSpecificationExecutor<ChargeRecord> {
    @Query(value = "select SUM(cn.fee) AS total FROM charge_record as cn WHERE cn.car_type = ?1 GROUP BY DAY(from_unixtime(cn.time/1000)) ORDER BY cn.time desc LIMIT 0 , ?2", nativeQuery = true)
    List<Object> statisticsTemp(Integer carType, Integer count);

    List<ChargeRecord> findByCarType(CarType carType);
}
