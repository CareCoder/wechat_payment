package com.itstyle.domain.car.manager;

import com.itstyle.domain.car.manager.enums.CarNumExtVo;
import com.itstyle.domain.car.manager.enums.CarNumType;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.utils.hibernate.BaseEntity;
import lombok.Data;
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
    private CarType carType;
    private Long time;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_num_id")
    private List<CarNumExtVo> carNumExtVos = new ArrayList<>();

    public String getUuid(CarNumType carNumType) {
        return carNumExtVos.stream().filter(e -> e.getCarNumType() == carNumType).findAny().get().getUuid();
    }
}
