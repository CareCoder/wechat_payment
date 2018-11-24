package com.itstyle.domain.caryard;

import lombok.Data;

@Data
public class CarYardName {
    private String parkingName;
    private Integer parkingNum;
    private Boolean isPrefabricationCharge = false;
    private Boolean unlicensedHandle = false;
    private Integer monthlyCarsOverDays;
    private Boolean fixedParkingSpace = false;
    private Boolean isAccurateMatching = false;
}
