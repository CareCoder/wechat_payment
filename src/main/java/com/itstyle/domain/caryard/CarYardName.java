package com.itstyle.domain.caryard;

import lombok.Data;

@Data
public class CarYardName {
    private String parkingName;
    private Integer parkingNum;
    private Boolean isPrefabricationCharge;
    private Boolean unlicensedHandle;
    private Integer monthlyCarsOverDays;
    private Boolean fixedParkingSpace;
    private Boolean isAccurateMatching;
}
