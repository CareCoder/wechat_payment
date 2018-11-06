package com.itstyle.exception;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class AssertUtil {

    public static void assertTrue(boolean b, Supplier<? extends  RuntimeException> supplier) {
        if(!b){
            throw supplier.get();
        }
    }

    public static void assertNotNull(Object o, Supplier<? extends  RuntimeException> supplier ){
        assertTrue(Objects.nonNull(o), supplier);
    }

    public static void assertNotEmpty(String str, Supplier<? extends  RuntimeException> supplier){
        assertTrue(StringUtils.isNotEmpty(str), supplier);
    }

    public static void assertNotEmpty(Collection collection, Supplier<? extends  RuntimeException> supplier){
        assertTrue(!CollectionUtils.isEmpty(collection), supplier);
    }

    public static void assertNotEmpty(String[] arr, Supplier<? extends  RuntimeException> supplier){
        if(ArrayUtils.isEmpty(arr)){
            throw supplier.get();
        }
    }

    public static void assertNotEmpty(String[] arr){
        assertNotEmpty(arr, RuntimeException::new);
    }

    public static void assertNotEmpty(Collection collection){
        assertTrue(!CollectionUtils.isEmpty(collection), RuntimeException::new);
    }

    public static void assertNotEmpty(Map map, Supplier<? extends  RuntimeException> supplier){
        assertTrue(!CollectionUtils.isEmpty(map), supplier);
    }


    public static void assertNotEmpty(String string) {
        assertNotEmpty(string, RuntimeException::new);
    }

    public static void assertTrue(boolean b) {
        assertTrue(b, RuntimeException::new);
    }

    public static void assertFalse(boolean b, Supplier<? extends RuntimeException> supplier) {
        if (b) {
            throw supplier.get();
        }
    }

    public static void assertNull(Object o){
        assertNull(o, RuntimeException::new);
    }

    public static void assertNull(Object o, Supplier<? extends RuntimeException> supplier) {
        if(Objects.nonNull(o)){
            throw supplier.get();
        }
    }


}
