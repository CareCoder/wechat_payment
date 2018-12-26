package com.itstyle.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 公共的工具类
 */
@Slf4j
public class CommonUtils {
    /**
     * 根据name值,获取枚举
     */
    public static <T> T fetchEnum(String name, Class<T> clazz) {
        try {
            Method valuesMethod = clazz.getMethod("values");
            T[] enums = (T[]) valuesMethod.invoke(null);
            for (T t : enums) {
                Class<?> tClass = t.getClass();
                Field nameField = tClass.getDeclaredField("name");
                nameField.setAccessible(true);
                String nameValue = (String) nameField.get(t);
                if (nameValue != null && nameValue.equals(name)) {
                    return t;
                }
            }
        } catch (Exception e) {
            log.error("fetchEnum error ", e);
        }
        return null;
    }
}
