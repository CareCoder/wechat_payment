package com.itstyle.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

/***
 * @author gechaoqing
 * 日期工具类
 */
@Slf4j
public class DateUtil {
    private static final String format = "yyyy/MM/dd HH:mm:ss";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(format);

    private static final String format2 = "yyyy/MM/dd";
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat(format2);
    /***
     * 获取某年某月的开始时间
     * @param year 年
     * @param month 月
     * @return 日期
     */
    public static Date getBeginTime(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate localDate = yearMonth.atDay(1);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        ZonedDateTime zonedDateTime = startOfDay.atZone(ZoneId.of("Asia/Shanghai"));
        return Date.from(zonedDateTime.toInstant());
    }

    /***
     * 获取某年某月的结束时间
     * @param year 年
     * @param month 月
     * @return 日期
     */
    public static Date getEndTime(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();
        LocalDateTime localDateTime = endOfMonth.atTime(23, 59, 59, 999);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Shanghai"));
        return Date.from(zonedDateTime.toInstant());
    }

    /***
     * 获取某月开始时间
     * @param date 输入日期
     * @return 日期
     */
    public static Date getMonthBiginTime(Date date){
        Calendar calendar = getCalendar(date);
        return getBeginTime(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1);
    }

    /***
     * 获取某月结束时间
     * @param date 输入日期
     * @return 日期
     */
    public static Date getMonthEndTime(Date date){
        Calendar calendar = getCalendar(date);
        return getEndTime(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1);
    }


    /***
     * 获取本周一的日期
     * @param date 输入日期
     * @return 日期
     */
    public static Date getMonday(Date date){
        return getDayOfWeek(date,1);
    }

    /***
     * 获取本周末的日期
     * @param date 输入日期
     * @return 日期
     */
    public static Date getSunDay(Date date){
        return getDayOfWeek(date,7);
    }

    private static Date getDayOfWeek(Date date,int add){
        Calendar calendar = getCalendar(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
        if(dayOfWeek==0){
            dayOfWeek=7;
        }
        calendar.add(Calendar.DATE,-dayOfWeek+add);
        return calendar.getTime();
    }

    private static Calendar getCalendar(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String format(Long time) {
        if (time == null) {
            return "";
        }
        return sdf.format(new Date(time));
    }

    public static String format2Date(Long time) {
        if (time == null) {
            return "";
        }
        return sdf2.format(new Date(time));
    }

    public static Date parse(String time) {
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            log.error("parse error ", e);
        }
        return null;
    }

    public static Date parse2Date(String time) {
        try {
            return sdf2.parse(time);
        } catch (ParseException e) {
            log.error("parse2Date error ", e);
        }
        return null;
    }

    /**
     * 计算当前时间之后的几个月是多久
     * @param time 当前时间
     * @param month 几个月
     * @return 计算当前时间之后的几个月的数据
     */
    public static Long calcAfterMonthTime(Long time, Integer month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTimeInMillis();
    }

    /**
     * 计算相差几个月
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public static Integer calcMonth(Long startTime, Long endTime) {
        try {
            Calendar c1 = Calendar.getInstance();
            c1.setTimeInMillis(startTime);
            Calendar c2 = Calendar.getInstance();
            c2.setTimeInMillis(endTime);
            int c2Value = c2.get(Calendar.YEAR) * 12 + c2.get(Calendar.MONTH);
            int c1Value = c1.get(Calendar.YEAR) * 12 + c1.get(Calendar.MONTH);
            return c2Value - c1Value;
        } catch (Exception e) {
            log.error("calcMonth", e);
            return 0;
        }
    }
}
