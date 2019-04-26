package com.itstyle.domain.car.manager;

import com.itstyle.domain.car.manager.enums.CarType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FixedCarManager {
    private CarType carType;
    private String customName;
    private String typeName;
    private Integer monthFee;
    private String remark;
    private boolean status = true;

    /**
     * 如果没取到数据则，默认返回此数据
     */
    public static List<FixedCarManager> defaultList() {
        List<FixedCarManager> list = new ArrayList<>();
        String[] names = {"月租车A", "月租车B", "月租车C"};
        CarType[] carTypes = {CarType.MONTH_CAR_A, CarType.MONTH_CAR_B, CarType.MONTH_CAR_C};
        for (int i = 0; i < names.length; i++) {
            FixedCarManager f = new FixedCarManager();
            f.setCarType(carTypes[i]);
            f.setCustomName(names[i]);
            f.setTypeName(names[i]);
            f.setMonthFee(0);
            f.setRemark("");
            f.setStatus(true);
            list.add(f);
        }
        return list;
    }
}
