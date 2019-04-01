package com.itstyle.vo.inition.response;

import com.itstyle.domain.caryard.CarYardName;
import lombok.Data;

@Data
public class CarYardNameResp {
    public String parkingName;
    public Integer parkingNum;
    public Integer totalParkingNum;
    public Boolean isPrefabricationCharge = false;
    public Boolean unlicensedHandle = false;
    public Integer centralFeeFreeTime;
    public Integer monthlyCarsOverDays;
    public Boolean fixedParkingSpace = false;
    public Boolean isAccurateMatching = false;

    public static CarYardNameResp build(CarYardName c) {
        CarYardNameResp r = new CarYardNameResp();
        r.parkingName = c.getParkingName();
        r.parkingNum = c.getParkingNum();
        r.isPrefabricationCharge = c.getIsPrefabricationCharge();
        r.unlicensedHandle = c.getUnlicensedHandle();
        r.centralFeeFreeTime = c.getCentralFeeFreeTime();
        r.monthlyCarsOverDays = c.getMonthlyCarsOverDays();
        r.fixedParkingSpace = c.getFixedParkingSpace();
        r.isAccurateMatching = c.getIsAccurateMatching();
        return r;
    }
}
