package com.itstyle.domain.car.manager;

import com.itstyle.domain.car.manager.enums.CarNumType;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.utils.hibernate.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "car_num", indexes = {@Index(name = "path_index", columnList = "path", unique = true)})
@DynamicUpdate
public class CarNumVo extends BaseEntity {
    private String carNum;
    private CarNumType carNumType;
    private CarType carType;
    private Long time;
    private String path;
    private String enterBigUuid;
    private String enterSmallUuid;
    private String leaveBigUuid;
    private String leaveSmallUuid;

    public void setUuid(CarNumType carNumType, String uuid) {
        if (carNumType == CarNumType.ENTER_BIG) {
            enterBigUuid = uuid;
        } else if (carNumType == CarNumType.ENTER_SMALL) {
            enterSmallUuid = uuid;
        } else if (carNumType == CarNumType.LEAVE_BIG) {
            leaveBigUuid = uuid;
        } else if (carNumType == CarNumType.LEAVE_SMALL) {
            leaveSmallUuid = uuid;
        }
    }

    public String getUuid(CarNumType carNumType) {
        if (carNumType == CarNumType.ENTER_BIG) {
            return enterBigUuid;
        } else if (carNumType == CarNumType.ENTER_SMALL) {
            return enterSmallUuid;
        } else if (carNumType == CarNumType.LEAVE_BIG) {
            return leaveBigUuid;
        } else if (carNumType == CarNumType.LEAVE_SMALL) {
            return leaveSmallUuid;
        }
        return "";
    }
}
