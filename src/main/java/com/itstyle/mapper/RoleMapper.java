package com.itstyle.mapper;

import com.itstyle.domain.account.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;

public interface RoleMapper {
    @Insert("insert into role(name) values(#{name})")
    @SelectKey(keyProperty = "id", before = false, statement = "SELECT LAST_INSERT_ID()", resultType = Long.class)
    Long insert(Role role);

    @Delete("delete from role where id in (<foreach item='id' collection='ids' separator=','>#{id}</foreach>)")
    Integer delete(@Param("ids")List<Long> ids);
}
