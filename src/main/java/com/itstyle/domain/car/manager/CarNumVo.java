package com.itstyle.domain.car.manager;

import com.itstyle.domain.car.manager.enums.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "car_num")
@DynamicUpdate
public class CarNumVo{
    @Id
    @GeneratedValue
    private Long id;

    private String carNum;
    private String shortCarNum;//省去省份的车牌号码
    private CarType carType;
    private CarColor carColor;
    private Integer fee;
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
    private Long time;
    private String enterPass;
    private String leavePass;
    private String enterWay;
    private Long lTime;//算是扩展字段,离开时间,为了提高性能添加这个字段,和扩展字段重复
    private Boolean record;//这次临时停车是否已经生成明细
    private Boolean fixedParkingSpace;//是否固定车位
    private Long stopTime;//停车时长, stopTime+time=缴费时间<=lTime 这个关系一定要理解
    private String paymentWay;//支付方式

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.ALL})
    @JoinColumn(name = "car_num_id"
            ,referencedColumnName = "id"
            ,foreignKey = @ForeignKey(
            name = "FK_child_to_parent",
            value = ConstraintMode.NO_CONSTRAINT))
    private List<CarNumExtVo> carNumExtVos = new ArrayList<>();

    public String getUuid(CarNumType carNumType) {
        return carNumExtVos.stream().filter(e -> e.getCarNumType() == carNumType).findAny().get().getUuid();
    }

    public CarNumVo buildQueryVo() {
        CarNumVo vo = new CarNumVo();
        vo.setShortCarNum(this.shortCarNum);
//        vo.setCarType(this.carType);
        vo.setTime(this.time);
        return vo;
    }

    public void setLTime(Long time) {
        if (time == null) {
            return;
        }
        this.lTime = time;
    }

    public void setEnterPass(String enterPass) {
        if (enterPass == null) {
            return;
        }
        this.enterPass = enterPass;
    }

    public void setLeavePass(String leavePass) {
        if (leavePass == null) {
            return;
        }
        this.leavePass = leavePass;
    }

    public void setEnterWay(String enterWay) {
        if (enterWay == null) {
            return;
        }
        this.enterWay = enterWay;
    }

    public void buildShortCarNum() {
        if (StringUtils.isNotEmpty(carNum)) {
            shortCarNum = carNum.substring(1);
        }
    }
}
