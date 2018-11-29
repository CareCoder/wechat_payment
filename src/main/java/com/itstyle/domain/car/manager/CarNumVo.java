package com.itstyle.domain.car.manager;

import com.itstyle.domain.car.manager.enums.CarColor;
import com.itstyle.domain.car.manager.enums.CarNumExtVo;
import com.itstyle.domain.car.manager.enums.CarNumType;
import com.itstyle.domain.car.manager.enums.CarType;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.BeanUtils;

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
    private CarType carType;
    private CarColor carColor;
    private int fee;
    private Long time;
    private String enterPass;
    private String leavePass;
    private String enterWay;
    private Long lTime;//算是扩展字段,离开时间,为了提高性能添加这个字段,和扩展字段重复

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_num_id")
    private List<CarNumExtVo> carNumExtVos = new ArrayList<>();

    public String getUuid(CarNumType carNumType) {
        return carNumExtVos.stream().filter(e -> e.getCarNumType() == carNumType).findAny().get().getUuid();
    }

    public CarNumVo getQueryVo() {
        CarNumVo vo = new CarNumVo();
        BeanUtils.copyProperties(this, vo);
        vo.setEnterPass(null);
        vo.setLeavePass(null);
        vo.setEnterWay(null);
        vo.setLTime(null);
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
}
