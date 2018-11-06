package com.itstyle.mapper;

import com.itstyle.domain.carinfo.CarInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.sql.Timestamp;
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
            @Result(property = "createTime", column = "create_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "modifyTime", column = "modify_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP)
    })
    @Select("SELECT * FROM car_info")
    @ResultType(List.class)
    List<CarInfo> getAll();

    @ResultMap("userResultMap")
    @Select("SELECT * FROM car_info WHERE id = #{id}")
    CarInfo getById(Long id);

    @ResultMap("userResultMap")
    @Select("SELECT * FROM car_info WHERE car_num = #{carNum}")
    CarInfo getByCarNum(String carNum);

    @Insert("INSERT INTO car_info(name,car_num,phone,remarks,create_time,modify_time,is_free,is_blackList) " +
            " VALUES(#{name},#{carNum},#{phone},#{remarks},#{createTime},#{modifyTime},#{isFree},#{isBlackList})")
    void insert(CarInfo carInfo);

    @Delete("DELETE FROM car_info WHERE id = #{id}")
    void delete(Long id);

    @UpdateProvider(type = CarInfoProvider.class, method = "update")
    void update(CarInfo carInfo);
}
