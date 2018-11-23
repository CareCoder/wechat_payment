package com.itstyle.domain.caryard;

import lombok.Data;

@Data
public class ResponsePassCarStatus {
    private Long id;
    private String channelName;
    private Long channelTypeId;
    private Integer entrance_tempCar_1;
    private Integer entrance_monlyCar_1;
    private Integer entrance_specialCar_1;
    private Integer entrance_tempCar_2;
    private Integer entrance_monlyCar_2;
    private Integer entrance_specialCar_2;
    private String channelTypeName;
    private String ip;
}
