package com.itstyle.domain.caryard;

import lombok.Data;

@Data
public class ResponsePassCarStatus {
    private Long id;
    private String channelName;
    private Long channelTypeId;
    private Integer tempCarA;
    private Integer tempCarB;
    private Integer tempCarC;
    private Integer monthCarA;
    private Integer monthCarB;
    private Integer monthCarC;
    private Integer vipCar;
    private String channelTypeName;
}
