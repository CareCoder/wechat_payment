package com.itstyle.utils;

import com.itstyle.domain.car.manager.FixedCarManager;
import com.itstyle.domain.car.manager.enums.CarType;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

public class BusinessUtils {
    public static String fetchCustomName(CarType carType, List<FixedCarManager> f) {
        if (carType != null) {
            Optional<FixedCarManager> any = f.stream()
                    .filter(e -> carType == e.getCarType())
                    .findAny();
            if (any.isPresent()) {
                return any.get().getCustomName();
            }else{
                return carType.getName();
            }
        }
        return "";
    }


    public static CarType fetchCarTypeName(String carType, List<FixedCarManager> f) {
        if (!StringUtils.isEmpty(carType)) {
            Optional<FixedCarManager> any = f.stream()
                    .filter(e -> e.getCustomName().equals(carType))
                    .findAny();
            if (any.isPresent()) {
                return any.get().getCarType();
            }
        }
        return null;
    }
}
