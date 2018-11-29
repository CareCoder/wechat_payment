package com.itstyle.domain.report;

import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.car.manager.enums.ChargeType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "charge_record")
public class ChargeRecord {
    @Id
    @GeneratedValue
    protected Long id;

    /**
     * 车牌号码
     */
    private String carNum;

    /**
     * 入场时间
     */
    private Long enterTime;

    /**
     * 出场时间
     */
    private Long leaveTime;

    /**
     * 收费金额
     */
    private Integer fee;

    /**
     * 收费类型
     */
    private ChargeType chargeType;

    /**
     * 折扣金额
     */
    private Integer discountAmount;

    /**
     * 收费员
     */
    private String chargePersonnel;

    /**
     * 车辆类型
     */
    private CarType carType;
}
