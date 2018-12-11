package com.itstyle.domain.caryard;

import lombok.Data;

@Data
public class ResponsePassCarStatusEdit {
    private Long id;
    private Long accessTypeId;
    private Integer blueCar;
    private Integer yellowCar;
    private Integer greenCar;
    private Integer blackCar;
    private Integer monlyCar_A;
    private Integer monlyCar_B;
    private Integer monlyCar_C;
    private Integer specialCar;
    private Long channelTypeId;
}
