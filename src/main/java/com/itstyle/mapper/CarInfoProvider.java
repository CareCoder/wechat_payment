package com.itstyle.mapper;

import com.itstyle.domain.carinfo.CarInfo;
import org.apache.ibatis.jdbc.SQL;

public class CarInfoProvider {
    public String update(CarInfo carInfo) {
        return new SQL() {
            {
                SELECT("*");
                FROM("tb_employee");
                if (carInfo.getName() != null) {
                    SET("name = #{name}");
                }
                if (carInfo.getCarNum() != null) {
                    SET("car_num = #{carNum}");
                }
                if (carInfo.getPhone() != null) {
                    SET("phone = #{phone}");
                }
                if (carInfo.getRemarks() != null) {
                    SET("remarks = #{remarks}");
                }
                if (carInfo.getCreateTime() != null) {
                    SET("create_time = #{createTime}");
                }
                if (carInfo.getModifyTime() != null) {
                    SET("modify_time = #{modifyTime}");
                }
                if (carInfo.getIsFree() != null) {
                    SET("is_free = #{isFree}");
                }
                if (carInfo.getIsBlackList() != null) {
                    SET("is_blacklist = #{isBlackList}");
                }
                WHERE("id = #{id}");
            }

        }.toString();
    }
}
