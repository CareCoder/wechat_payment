package com.itstyle.domain.report;

import com.itstyle.domain.car.manager.enums.CarType2;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 删除记录
 */
@Data
@Entity
@Table(name = "delete_record")
public class DeleteRecord {
    @Id
    @GeneratedValue
    protected Long id;

    /**
     * 车牌号码
     */
    private String carNum;

    /**
     * 删除时间
     */
    private Long deleteTime;

    /**
     * 删除的车的类型
     */
    private CarType2 carType2;
}
