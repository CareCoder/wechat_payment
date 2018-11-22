package com.itstyle.vo.inition.response;

import com.itstyle.domain.caryard.CarYardName;
import com.itstyle.vo.syncarinfo.response.SynCarInfo;

import java.util.List;

public class Inition {
    public CarYardName carYardName;
    public List<AccessAuthoritySetup> accessAuthoritySetup;
    public ChargeSetting chargeSetting;
    public ChargeRule chargeRule;
    public SynCarInfo synCarInfo;
}
