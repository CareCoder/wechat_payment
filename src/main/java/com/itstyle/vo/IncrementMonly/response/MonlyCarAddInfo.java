package com.itstyle.vo.Incrementmonly.response;

import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.utils.DateUtil;

public class MonlyCarAddInfo {
    public String plateID;//	车牌号码

    public String name;//	车主姓名

    public String phoneNumber;//车主的联系方式

    public String monlyCardTimeStart;//	时间格式： yyyy/MM/dd HH:mm:ss

    public String monlyCardTimeEnd;//时间格式： yyyy/MM/dd HH:mm:ss

    public Integer monlyCarPlateColor;//	月卡车辆车牌颜色(蓝/黄/黑)

    public static MonlyCarAddInfo convert(MonthCarInfo monthCarInfo) {
        MonlyCarAddInfo mcai = new MonlyCarAddInfo();
        mcai.plateID = monthCarInfo.getCarNum();
        mcai.name = monthCarInfo.getName();
        mcai.phoneNumber = monthCarInfo.getPhone();
        mcai.monlyCardTimeStart = DateUtil.format(monthCarInfo.getStartTime());
        mcai.monlyCardTimeEnd = DateUtil.format(monthCarInfo.getEndTime());
        mcai.monlyCarPlateColor = monthCarInfo.getCarColor() != null ? monthCarInfo.getCarColor().ordinal() : null;
        return mcai;
    }
}
