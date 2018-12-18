package com.itstyle.vo.inition.response;

import com.itstyle.domain.caryard.CarYardName;
import com.itstyle.vo.syncarinfo.response.SynCarInfo;

import java.util.List;

public class Inition {
    public CarYardName carYardName;
    public List<AccessAuthoritySetup> accessAuthoritySetup;
    public Object chargeRule;
    public VehicleManagement vehicleManagement;
    public ImageDisplay imageDisplay; //针对LCD广告图片发布，界面参考新增功能文档
    public TextDisplay textDisplay; //针对LED文字信息发布，界面参数新增功能文档
}
