package com.itstyle.mapper;

import com.itstyle.domain.carinfo.CarInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface CarInfoMapper {
    @Results(id = "userResultMap", value = {
            @Result(property = "id", column = "id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "carNum", column = "car_num", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "phone", column = "phone", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "remarks", column = "remarks", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "isFree", column = "is_free", javaType = Boolean.class, jdbcType = JdbcType.TINYINT),
            @Result(property = "isBlackList", column = "is_blackList", javaType = Boolean.class, jdbcType = JdbcType.TINYINT),
            @Result(property = "createTime", column = "create_time", javaType = Long.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "modifyTime", column = "modify_time", javaType = Long.class, jdbcType = JdbcType.TIMESTAMP)
    })
    @Select("SELECT * FROM car_info")
    @ResultType(List.class)
    List<CarInfo> getAll();

    @Insert("INSERT INTO carinfo(name,carNum,phone,remarks,createTime,modifyTime,isFree,isBlackList) +" +
            " VALUES(#{name},#{carNum},#{phone},#{remarks},#{createTime},#{modifyTime},#{isFree},#{isBlackList})")
    void insert(CarInfo carInfo);
}
