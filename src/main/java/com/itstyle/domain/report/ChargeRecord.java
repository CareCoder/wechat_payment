package com.itstyle.domain.report;

import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.car.manager.enums.ChargeSituation;
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
     * 收费情况
     */
    private ChargeSituation chargeSituation;

    /**
     * 应收金额
     */
    private Integer receivableFee;

    /**
     * 折扣金额
     */
    private Integer discountAmount;

    /**
     * 收费员
     */
    private String chargePersonnel;

    /**
     * 车辆类型(TEMP_CAR_A,代表临时车,MONTH_CAR_A代表月租车.)
     */
    private CarType carType;

    /**
     * 车辆真实类型
     */
    private CarType carRealType;

    /**
     * 收费时间
     */

    private Long time;

    /**
     * 关联的id 不一定指定关联
     */
    private Long associateId;


    public void setFee(Integer fee) {
        this.fee = fee;
        if (discountAmount != null && fee != null) {
            receivableFee = discountAmount + fee;
        }
    }
}
