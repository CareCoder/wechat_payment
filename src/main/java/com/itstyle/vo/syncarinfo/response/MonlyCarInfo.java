package com.itstyle.vo.syncarinfo.response;

import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.utils.DateUtil;
import lombok.Data;

@Data
public class MonlyCarInfo {
    private String plateID;//车牌号码

    private String name;//车主姓名

    private String phoneNumber;//车主的联系方式

    private String monlyCardTimeStart;//时间格式： yyyy/MM/dd HH:mm:ss

    private String monlyCardTimeEnd;//时间格式： yyyy/MM/dd HH:mm:ss

    private Integer monlyCarPlateColor;//月卡车辆车牌颜色(蓝/黄/黑)

    private Integer vehicleType; //月租车里面的哪一种车辆类型

    public static MonlyCarInfo convert(MonthCarInfo mc) {
        MonlyCarInfo ml = new MonlyCarInfo();
        ml.monlyCardTimeEnd = DateUtil.format(mc.getEndTime());
        ml.monlyCardTimeStart = DateUtil.format(mc.getStartTime());
        ml.monlyCarPlateColor = mc.getCarColor() != null ? mc.getCarColor().ordinal() : null;
        ml.name = mc.getName();
        ml.phoneNumber = mc.getPhone();
        ml.plateID = mc.getCarNum();
        ml.vehicleType = mc.getCarType() != null ? mc.getCarType().ordinal() : null;
        return ml;
    }

}
