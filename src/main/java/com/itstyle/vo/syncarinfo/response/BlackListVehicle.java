package com.itstyle.vo.syncarinfo.response;

import com.itstyle.domain.car.manager.CarInfo;
import lombok.Data;

@Data
public class BlackListVehicle {
    private String blackListVehicle_plate;//	黑名单上车辆的车牌号
    private Integer blackListVehicle_plateColor;// 黑名单上车辆的车牌颜色(蓝/黄/黑)
    private String blackListVehicle_info;//	黑名单入场时显示的原因信息

    public static BlackListVehicle convert(CarInfo carInfo) {
        BlackListVehicle bl = new BlackListVehicle();
        bl.blackListVehicle_plate = carInfo.getCarNum();
        bl.blackListVehicle_plateColor = carInfo.getCarColor() != null ? carInfo.getCarColor().ordinal() : null;
        bl.blackListVehicle_info = carInfo.getRemarks();
        return bl;
    }

}
