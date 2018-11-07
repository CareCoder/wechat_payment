package com.itstyle.mapper;

import com.itstyle.domain.SysLogger;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;

public interface LogMapper {

    @Insert({
            "<script>",
            "insert into log(username, action, describe, log_date) ",
            "values <foreach item='log' collection='logs' separator=','>",
            "(#{log.username}, #{log.action}, #{log.describe}, #{log.logDate})</foreach>",
            "</script>"
    })
    Long insertBatch(@Param("logs") List<SysLogger> logs);

    @Results(id = "logResultMap", value = {
            @Result(property = "username", column = "username", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "action", column = "action", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "describe", column = "describe", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "logDate", column = "log_date", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP)
    })
    @Select("select username, action, describe, log_date from log order by log_date desc")
    List<SysLogger> getAll();
}
