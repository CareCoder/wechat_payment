package com.itstyle.vo.syncarinfo.response;

import com.itstyle.domain.car.manager.CarInfo;
import lombok.Data;

@Data
public class FreeVehicle {
    private String freeVehicle_plate;//	免费车辆的车牌号
    private String freeVehicle_plateColor;//	免费车辆的车牌颜色(蓝/黄/黑)

    public static FreeVehicle convert(CarInfo carInfo) {
        FreeVehicle fv = new FreeVehicle();
        fv.freeVehicle_plate = carInfo.getCarNum();
        fv.freeVehicle_plateColor = carInfo.getCarColor() != null ? carInfo.getCarColor().toString() : "";
        return fv;
    }

}
