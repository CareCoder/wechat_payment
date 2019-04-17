package com.itstyle.vo.incrementmonly.response;

import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.utils.DateUtil;

public class MonlyCarRenewInfo {
    public String plateID;//	车牌号码

    public String monlyCardTimeStart;//	时间格式： yyyy/MM/dd HH:mm:ss

    public String monlyCardTimeEnd;//	时间格式： yyyy/MM/dd HH:mm:ss

    public String monlyCardTimeRenew;//	时间格式： yyyy/MM/dd HH:mm:ss(注意这里在保存的时候是时间是累加的字符串，但这里只下发最新的续费时间)

    public Integer monlyCarPlateColor;//	月卡车辆车牌颜色(蓝/黄/黑)

    public Integer vehicleType; //月租车里面的哪一种车辆类型

    public static MonlyCarRenewInfo convert(MonthCarInfo monthCarInfo) {
        MonlyCarRenewInfo mcri = new MonlyCarRenewInfo();
        mcri.plateID = monthCarInfo.getCarNum();
        mcri.monlyCardTimeStart = DateUtil.format(monthCarInfo.getStartTime());
        mcri.monlyCardTimeEnd = DateUtil.format(monthCarInfo.getEndTime());
        mcri.monlyCardTimeRenew = DateUtil.format(System.currentTimeMillis());
        mcri.monlyCarPlateColor = monthCarInfo.getCarColor() != null ? monthCarInfo.getCarColor().ordinal() : null;
        mcri.vehicleType = monthCarInfo.getCarType() != null ? monthCarInfo.getCarType().ordinal() : null;
        return mcri;
    }
}
