package com.itstyle.domain.caryard;

import lombok.Data;

@Data
public class ResponsePassCarStatus {
    private Long id;
    private String channelName;
    private Long channelTypeId;
    private Integer blueCar;
    private Integer yellowCar;
    private Integer greenCar;
    private Integer blackCar;
    private Integer monlyCar_A;
    private Integer monlyCar_B;
    private Integer monlyCar_C;
    private Integer specialCar;
    private String channelTypeName;
    private String ip;
    private String camera;
}
