package com.itstyle.domain.caryard;

import lombok.Data;

@Data
public class CarYardName {
    private String parkingName;
    private Integer parkingNum;
    private Boolean isPrefabricationCharge;
    private Boolean unlicensedHandle;
    private Integer centralFeeFreeTime;
    private Boolean fixedParkingSpace;
    private Boolean isAccurateMatching;
}
