package com.itstyle.domain.caryard;

import lombok.Data;

@Data
public class CarYardName {
    private String parkingName;
    private Integer parkingNum;
    private boolean isPrefabricationCharge;
    private boolean unlicensedHandle;
    private Integer monthlyCarsOverDays;
    private boolean fixedParkingSpace;
    private boolean isAccurateMatching;
}
