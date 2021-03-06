package com.itstyle.mapper;

import com.itstyle.domain.car.manager.CarNumVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CarNumMapper extends JpaRepository<CarNumVo, Long>, JpaSpecificationExecutor<CarNumVo> {
    @Query(value = "select COUNT(1) AS total FROM car_num as cn GROUP BY DAY(from_unixtime(cn.time/1000)) ORDER BY cn.time desc LIMIT 0 , ?1", nativeQuery = true)
    List<Object> statisticsAccess(Integer count);

    @Query(value = "SELECT * FROM car_num cn WHERE id IN ( SELECT MAX(id) FROM car_num WHERE l_time IS NULL GROUP BY short_car_num )" +
            " and if(?1 is not null,car_type=?1 , 1 = 1 ) " +
            " and if(?2 is not null,car_num like CONCAT('%',?2,'%') , 1 = 1 ) " +
            " and if(?3 is not null,?3 <= time, 1 = 1 ) " +
            " and if(?4 is not null,?4 >= time, 1 = 1 ) " +
            " and if(?5 is not null,car_type<=?5, 1 = 1 ) " +
            " and if(?6 is not null,record is null, 1 = 1 ) " +
            " ORDER BY id DESC limit ?7, ?8" ,nativeQuery = true)
    List<CarNumVo> queryComplex(Integer carType, String carNum, Long startTime, Long endTime, Integer carTypeLimit, Boolean record, Integer page, Integer limit);

    @Query(value = "SELECT count(1) from (SELECT DISTINCT short_car_num from car_num cn where cn.l_time is null " +
            " and if(?1 is not null,car_type=?1 , 1 = 1 ) " +
            " and if(?2 is not null,car_num like CONCAT('%',?2,'%') , 1 = 1 ) " +
            " and if(?3 is not null,car_type<=?3, 1 = 1 ) " +
            " and if(?4 is not null,?4 <= time, 1 = 1 ) " +
            " and if(?5 is not null,?5 >= time, 1 = 1 ) " +
            " and if(?6 is not null,record is null, 1 = 1 ) ) as c", nativeQuery = true)
    Long distincCount(Integer carType, String carNum, Integer carTypeLimit, Long startTime, Long endTime, Boolean record);

    @Modifying
    @Transactional
    @Query("delete from CarNumVo vo where vo.lTime is null and vo.shortCarNum = ?1")
    Integer deleteExceptionData(String shortCarNum);

    @Modifying
    @Transactional
    @Query("delete from CarNumVo vo where vo.lTime is null and vo.shortCarNum = ?1 and vo.time < ?2")
    void deleteUnleaveCar(String shortCarNum, Long time);

}
