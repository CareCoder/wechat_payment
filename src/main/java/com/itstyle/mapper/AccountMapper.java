package com.itstyle.mapper;

import com.itstyle.domain.account.Account;
import com.itstyle.domain.account.req.RequestAccount;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;

public interface AccountMapper {

    @Insert("insert into account(username, account, password, role_id, create_time, remark) " +
            "values(#{username}, #{account}, #{password}, #{roleId}, #{createTime}, #{remark})")
    @SelectKey(keyProperty = "id", before = false, statement = "SELECT LAST_INSERT_ID()", resultType = Long.class)
    Long insert(Account account);

    @Update("update account set password = #{newPassword}, " +
            "role_id = #{roleId}, update_time = #{updateTime}, remark = #{remark} where id = #{id}")
    Integer edit(RequestAccount account);

    @Delete("delete from account where id = #{id}")
    Integer delete(Long id);

    @Results(id = "accountResultMap", value = {
            @Result(property = "id", column = "id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Result(property = "username", column = "username", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "account", column = "account", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "roleId", column = "role_id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "remark", column = "remark", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "password", column = "password", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "roleName", column = "role_name", javaType = String.class, jdbcType = JdbcType.VARCHAR)

    })
    @Select("select a.id, a.username, a.account, a.password, a.role_id, a.create_time, a.remark, b.name as role_name from account a " +
            "left join role b on a.role_id = b.id")
    @ResultType(List.class)
    List<Account> getAll();

    @ResultMap("accountResultMap")
    @Select({
            "<script>",
            "select id, username, account, password, role_id, create_time, remark from account where id = #{id}",
            "</script>"
    })
    Account selectById(Long id);
}
