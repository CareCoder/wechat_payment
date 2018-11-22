package com.itstyle.vo.charges.reponse;

import lombok.Data;

import java.util.List;

@Data
public class ChargesResponse<T> {
    private Integer chargeModel;
    private List<T> data;
}
