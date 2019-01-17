package com.itstyle.mapper;

import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.report.ChargeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ChargeRecordMapper extends JpaRepository<ChargeRecord, Long>, JpaSpecificationExecutor<ChargeRecord> {
    @Query(value = "select SUM(cn.fee) AS total FROM charge_record as cn WHERE cn.car_type = ?1 GROUP BY DAY(from_unixtime(cn.time/1000)) ORDER BY cn.time desc LIMIT 0 , ?2", nativeQuery = true)
    List<Object> statisticsTemp(Integer carType, Integer count);

    List<ChargeRecord> findByCarType(CarType carType);

    @Query(value = "select SUM(cn.fee) AS total_fee , SUM(cn.receivable_fee) AS total_receivable_fee , SUM(cn.discount_amount) AS total_discount_amount " +
            "FROM charge_record as cn " +
            "WHERE if(?1 is not null,charge_type=?1 , 1 = 1 ) " +
            "AND if(?2 is not null,car_type=?2 , 1 = 1 ) " +
            "AND if(?3 is not null,car_real_type=?3 , 1 = 1 ) " +
            "AND if(?4 is not null,car_num like CONCAT('%',?4,'%') , 1 = 1 ) " +
            "AND if(?5 is not null,charge_personnel=?5 , 1 = 1 ) " +
            "AND if(?6 is not null,time>=?6 , 1 = 1 ) " +
            "AND if(?7 is not null,time<=?7 , 1 = 1 ) ", nativeQuery = true)
    List<Object[]> statistics(Integer chargeType, Integer carType, Integer carRealType, String carNum, String chargePersonnel, Long startTime, Long endTime);
}
