package com.itstyle.mapper;

import com.itstyle.domain.bean.CarInfo;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface CarInfoMapper {
    @Results(id = "userResultMap", value = {
            @Result(property = "id", column = "id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "carNum", column = "car_num", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "phone", column = "phone", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "remarks", column = "remarks", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "createTime", column = "create_time", javaType = Long.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "modifyTime", column = "modify_time", javaType = Long.class, jdbcType = JdbcType.TIMESTAMP)
    })
    @Select("SELECT * FROM car_info")
    @ResultType(List.class)
    List<CarInfo> getAll();
}
