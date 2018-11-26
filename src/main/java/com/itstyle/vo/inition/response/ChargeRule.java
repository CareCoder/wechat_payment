package com.itstyle.vo.inition.response;

import com.itstyle.vo.charges.reponse.ChargeRuleVo;
import lombok.Data;

import java.util.List;

@Data
public class ChargeRule {
    List<ChargeRuleVo> chargeRuleList;
}
