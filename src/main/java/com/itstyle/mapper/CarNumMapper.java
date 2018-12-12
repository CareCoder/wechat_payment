package com.itstyle.mapper;

import com.itstyle.domain.car.manager.CarNumQueryVo;
import com.itstyle.domain.car.manager.CarNumVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarNumMapper extends JpaRepository<CarNumVo, Long>, JpaSpecificationExecutor<CarNumVo> {
    @Query(value = "select COUNT(1) AS total FROM car_num as cn GROUP BY DAY(from_unixtime(cn.time/1000)) ORDER BY cn.time desc LIMIT 0 , ?1", nativeQuery = true)
    List<Object> statisticsAccess(Integer count);

    //    @Query(value = "SELECT * FROM car_num cn WHERE time IN ( SELECT MAX(time) FROM car_num GROUP BY car_num ) and cn.l_time is null GROUP BY car_num UNION SELECT * from car_num cn2 WHERE cn2.l_time is not null" +
//            "where if(#{#queryVo.carType} is not null,car_type=#{#queryVo.carType} ,1=1)" +
//            "and if(#{#queryVo.startTime} is not null,time between #{#queryVo.startTime} and #{#queryVo.endTime},1=1)" +
//            "and if(#{#queryVo.leaveStartTime} is not null,time between #{#queryVo.leaveStartTime} and #{#queryVo.leaveEndTime},1=1)" +
//            "and if(#{#queryVo.isEnter} is not null,l_time is null,1=1)" +
//            "and if(#{#queryVo.leavePass} is not null,leave_pass=#{#queryVo.leavePass},1=1)", nativeQuery = true)
    @Query(value = "SELECT * FROM car_num cn WHERE time IN ( SELECT MAX(time) FROM car_num GROUP BY car_num ) and cn.l_time is null GROUP BY car_num UNION SELECT * from car_num cn2 WHERE cn2.l_time is not null " +
            " and if(?1 is not null,car_type=?1 , 1 = 1 ) " ,nativeQuery = true)
    List<CarNumVo> queryComplex(Integer carType);

    @Query(value = "SELECT count(1) from (SELECT DISTINCT car_num from car_num) as cn", nativeQuery = true)
    Long distincCount();
}
