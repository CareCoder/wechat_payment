package com.itstyle.utils;

public class FeeUtil {
    public static String convert(int fee) {
        try {
            double feeD= (double)fee / 100;
            String feeStr = String.valueOf(feeD);
            if (feeStr.contains(".")) {
                int indexDot = feeStr.indexOf(".");
                String rest = feeStr.substring(indexDot, feeStr.length());
                for (int i = rest.length(); i < 3; i++) {
                    feeStr += "0";
                }
            }else{
                feeStr += ".00";
            }
            return feeStr;
        } catch (Exception e) {
            return "0.00";
        }
    }

    /**
     * 将秒数转换为日时分秒，
     * @param second
     * @return
     */
    public static String secondToTime(long second){
        second = second / 1000; //毫秒 转 秒
        long days = second / 86400;            //转换天数
        second = second % 86400;            //剩余秒数
        long hours = second / 3600;            //转换小时
        second = second % 3600;                //剩余秒数
        long minutes = second /60;            //转换分钟
        if(days>0){
            return days + "天" + hours + "小时" + minutes + "分";
        }else if(hours > 0){
            return hours + "小时" + minutes + "分";
        }else{
            return minutes + "分";
        }
    }


    public static void main(String[] args) {
        System.out.println(FeeUtil.convert(0));
        System.out.println(FeeUtil.convert(1));
        System.out.println(FeeUtil.convert(13));
        System.out.println(FeeUtil.convert(123));
        System.out.println(FeeUtil.convert(1235));
    }
}
