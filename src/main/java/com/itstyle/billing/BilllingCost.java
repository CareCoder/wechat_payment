package com.itstyle.billing;


/*
*       根据传入的参数计算出当前的费用
* */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class BilllingCost {


    //车牌颜色的定义部分
//    public static final int LC_UNKNOWN      = 0;             //未知
    public static final int LC_BLUE         = 0;             //蓝色
    public static final int LC_YELLOW       = 1;             //黄色
    public static final int LC_BLACK        = 2;             //黑色
    public static final int LC_GREEN        = 3;             //绿色

    //车牌类型的定义部分
    public static final int LT_UNKNOWN      = 0;             //未知车牌
    public static final int LT_BLUE         = 1;             //蓝牌小汽车
    public static final int LT_BLACK        = 2;             //黑牌小汽车
    public static final int LT_YELLOW       = 3;             //单排黄牌
    public static final int LT_YELLOW2      = 4;            //双排黄牌（大车尾牌，农用车）
    public static final int LT_POLICE       = 5;            //警车车牌
    public static final int LT_ARMPOL       = 6;            //武警车牌
    public static final int LT_INDIVI       = 7;            //个性化车牌
    public static final int LT_ARMY         = 8;            //单排军车牌
    public static final int LT_ARMY2        = 9;            //双排军车牌
    public static final int LT_EMBASSY      = 10;           //使馆车牌
    public static final int LT_HONGKONG     = 11;           //香港进出中国大陆车牌
    public static final int LT_TRACTOR      = 12;           //农用车牌
    public static final int LT_COACH        = 13;           //教练车牌
    public static final int LT_MACAO        = 14;           //澳门进出中国大陆车牌
    public static final int LT_ARMPOL2      = 15;           //双层武警车牌
    public static final int LT_ARMPOL_ZONGDUI = 16;         // 武警总队车牌
    public static final int LT_ARMPOL2_ZONGDUI = 17;        // 双层武警总队车牌
    ////////////////////

    public static final String[] plateColorText = {"蓝色", "黄色", "黑色", "绿色"};


    /*
    * 1.按次收费: 每次固定金额
    * 2.标准收费:       CappingAmount       封顶金额
    *                   FreeTime            免费时间
    *                   FirstTime           首次时间
    *                   FirstAmount         首次金额
    *                   IncreasingTime      递增时间
    *                   IncreasingAmount    递增金额
    *
    *
    * 3.高峰期+工作日 重点需要注意先走完首段收费时间，在根据这个时间点来判断是否处在高峰期/非高峰期内
    *
    *                    CappingAmount       封顶金额
     *                   FreeTime            免费时间
     *                   PeakTimeStart       高峰开始时间
    *                    PeakTimeEnd         高峰结束时间
    *
    *
    * 4.白天晚上模式 类似工作日的高峰时间 白天：6:00--20:00
    *
    *
    * */

    private BillingRules billingRules;


    /**
     *
     * @param BillingModel      计费模式
     * @param  LC_COLOR          车牌颜色类型
     */
    public BilllingCost(int BillingModel, int LC_COLOR, int CappingAmount, int FreeTime){
        if(BillingModel == 1) {         //按次
            billingRules = new BillingRules(BillingModel, CappingAmount, FreeTime, LC_COLOR);
        }else if(BillingModel == 2) {   //标准收费
            billingRules = new BillingRules(BillingModel, CappingAmount, FreeTime, LC_COLOR);
        }else if(BillingModel == 3) {   //深圳收费
            billingRules = new BillingRules(BillingModel, CappingAmount, FreeTime, LC_COLOR);
        }else if(BillingModel == 4) {   //白天夜间收费标准
            billingRules = new BillingRules(BillingModel, CappingAmount, FreeTime, LC_COLOR);
        }
    }

    public BillingRules getBillingRules(){
        return this.billingRules;
    }

    /**
     * ----------------------------车牌颜色------入场时间------
     * 根据传入的参数计算出停车费用
     * 根据传入的日期来判断当天是否是工作日/非工作日
     * 还差的功能部分为节假日判断，这个需要从服务端下发数据，此功能暂定...
     */

    public double ParkingCost(int plateColor, String entryTime, int parkingHour, int parkMin){

        double parkingAmount = 0;

        if(billingRules.BilllingModel == 1){
            if(billingRules.commonModel.optionalCycle == 0) {
                parkingAmount = getParkingCostByOptionalDay(plateColor, entryTime, parkingHour, parkMin);
            }else if(billingRules.commonModel.optionalCycle == 1){
                parkingAmount = getParkingCostByOptionalHour(plateColor, entryTime, parkingHour, parkMin);
            }
        }else if(billingRules.BilllingModel == 2){
            if(billingRules.commonModel.optionalCycle == 0){
                parkingAmount = getParkingCostByOptionalDay(plateColor, entryTime, parkingHour, parkMin);
            }else if(billingRules.commonModel.optionalCycle == 1){
                parkingAmount = getParkingCostByOptionalHour(plateColor, entryTime, parkingHour, parkMin);
            }
        }else if(billingRules.BilllingModel == 3){
            if(billingRules.peakModel.optionalCycle == 0){
                parkingAmount = getParkingCostByOptionalDay(plateColor, entryTime, parkingHour, parkMin);
            }else if(billingRules.peakModel.optionalCycle == 1){
                parkingAmount = getParkingCostByOptionalHour(plateColor, entryTime, parkingHour, parkMin);
            }
        }else if(billingRules.BilllingModel == 4){
            parkingAmount = getParkingCostByOptionalDay(plateColor, entryTime, parkingHour, parkMin);
        }
        return parkingAmount;
    }

    /**
     * 这里只针对24小时的可选周期
     */

    public double getParkingCostByOptionalHour(int plateColor, String entryTime, int parkingHour, int parkMin){

        double parkingAmount = 0;
        if(parkingHour * 60 + parkMin <= 24*60){            //在24小时内
            parkingAmount = getCurrentDayEndParkingCost(plateColor, entryTime, parkingHour, parkMin);
        }else{
            //这里针对超过24小时临界点的逻辑 总的计费方式为 ：先计算24小时的费用再按照正常的流程来递加
//            double currentDayAmount = billingRules.CappingAmount;
            double temp24HourAmount = getCurrentDayEndParkingCost(plateColor, entryTime, 24, 0);
            double currentDayAmount = (temp24HourAmount > billingRules.CappingAmount) ? billingRules.CappingAmount : temp24HourAmount;

            //这里计算出入场的时候是周几
            int dayOfWeek = getWhichDayFromEntryTime(entryTime);
            int remainingTime = (parkingHour * 60 + parkMin) - 24*60;
            //再计算出具体停车的天数
            int parkingDays = remainingTime/(24*60);
            //这里的金额为整天停车的封顶金额
            double amount1 = getCappingAmountByAllDays(entryTime, parkingDays);
            //这个时间为除去整数的天数，余下当天的分钟数
            int overTime = remainingTime%(24*60);
            //根据当前是星期几和停车的天数计算出下一天开始是否为工作日/非工作日
            if( billingRules.BilllingModel == 3){
                if(((dayOfWeek + parkingDays + 1) % 7) >= 1 && ((dayOfWeek + parkingDays + 1) % 7) < 6) {
                    billingRules.peakModel.isWorkingDay = true;            //这里需要先计算出是否工作日/非工作日
                }else{
                    billingRules.peakModel.isWorkingDay = false;
                }
            }
            if(billingRules.BilllingModel == 1){        //如果是按次收费，直接返回的是封顶金额
                parkingAmount = billingRules.CappingAmount + amount1;
            }else if(billingRules.BilllingModel == 2){  //标准模式收费
                if (overTime%billingRules.commonModel.IncreasingTime > 0) {
                    parkingAmount += billingRules.commonModel.IncreasingAmount;
                }
                parkingAmount += (overTime / billingRules.commonModel.IncreasingTime) * billingRules.commonModel.IncreasingAmount;
                parkingAmount = (parkingAmount >= billingRules.CappingAmount) ? billingRules.CappingAmount : parkingAmount;
                parkingAmount += amount1;
            }else if(billingRules.BilllingModel == 3){      //高峰+工作日模式
                if (billingRules.peakModel.isWorkingDay) {
                    //工作日入场
                    int currentHour = getPrefixHour(entryTime);
                    int currentMin = getPrefixMin(entryTime);
                    do {
                        if (isBetweenPeakMin((currentHour * 60 + currentMin), billingRules.peakModel.PeakTimeStart * 60, billingRules.peakModel.PeakTimeEnd * 60)) {
                            //从非高峰期内进入高峰时期内
                            currentHour += billingRules.peakModel.peak.IncreasingTime / 60;
                            currentMin += billingRules.peakModel.peak.IncreasingTime % 60;
                            overTime -= billingRules.peakModel.peak.IncreasingTime;          //每次都需要把递增过后的剩余时间给计算出来
                            parkingAmount += billingRules.peakModel.peak.IncreasingAmount;
                            } else {
                            //从高峰期内进入非高峰时期内
                            currentHour += billingRules.peakModel.nonPeak.IncreasingTime / 60;
                            currentMin += billingRules.peakModel.nonPeak.IncreasingTime % 60;
                            overTime -= billingRules.peakModel.nonPeak.IncreasingTime;          //每次都需要把递增过后的剩余时间给计算出来
                            parkingAmount += billingRules.peakModel.nonPeak.IncreasingAmount;
                            currentHour = (currentHour >= 24) ? 0 : currentHour;
                        }
                    }while(overTime > 0);
                        parkingAmount = (parkingAmount >= billingRules.CappingAmount) ? billingRules.CappingAmount : parkingAmount;
                        parkingAmount += amount1;
                    } else {    //非工作日
                        if (overTime%billingRules.peakModel.nonWorkingDay.IncreasingTime > 0) {
                            parkingAmount += billingRules.peakModel.nonWorkingDay.IncreasingAmount;
                        }
                        parkingAmount += (overTime/billingRules.peakModel.nonWorkingDay.IncreasingTime)*billingRules.peakModel.nonWorkingDay.IncreasingAmount;
                        parkingAmount = (parkingAmount >= billingRules.peakModel.noWorkDayCappingAmount) ? billingRules.peakModel.noWorkDayCappingAmount : parkingAmount;
                        parkingAmount += amount1;
                    }
            }
            parkingAmount += currentDayAmount;
        }
        return parkingAmount;
    }


    /**
     *  这里只针对1天的可选周期
     * @param plateColor
     * @param entryTime
     * @param parkingHour
     * @param parkMin
     * @return
     */

    public double getParkingCostByOptionalDay(int plateColor, String entryTime, int parkingHour, int parkMin){

        double parkingAmount = 0;

        //第一步，先将临界点的天数给确定出来
        String dayEndTimeStr = getDayEndTime(entryTime);
        SimpleDateFormat format= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dayEndData = new Date(dayEndTimeStr);
        Date EntryData = new Date(entryTime);                       //入场的时间date
        long ms = dayEndData.getTime() - EntryData.getTime();
        int dayEndMin = (int)ms/(1000*60);

        if(parkingHour * 60 + parkMin <= dayEndMin){
            //没有超过临界点的天数
            parkingAmount = getCurrentDayEndParkingCost(plateColor, entryTime, parkingHour, parkMin);
        }else{
            //这里针对超过临界点的逻辑 总的计费方式为 ：先计算出当天的费用， 然后再看超出的时间为多少天，超过1天的当天取封顶价，不足一天的按照正常的流程来递加
            //这里先计算到到23：59：59的费用
            double currentDayAmount = getCurrentDayEndParkingCost(plateColor, entryTime, dayEndMin/60, dayEndMin%60);
            //这里计算出入场的时候是周几
            int dayOfWeek = getWhichDayFromEntryTime(entryTime);
            int remainingTime = (parkingHour * 60 + parkMin) - dayEndMin;
            //再计算出具体停车的天数xz
            int parkingDays = remainingTime/(24*60);
            //这里的金额为整天停车的封顶金额
            double amount1 = getCappingAmountByAllDays(entryTime, parkingDays);
            //这个时间为除去整数的天数，余下当天的分钟数
            int overTime = remainingTime%(24*60);
            //根据当前是星期几和停车的天数计算出下一天开始是否为工作日/非工作日
            if( billingRules.BilllingModel == 3){
                if(((dayOfWeek + parkingDays + 1) % 7) >= 1 && ((dayOfWeek + parkingDays + 1) % 7) < 6) {
                    billingRules.peakModel.isWorkingDay = true;            //这里需要先计算出是否工作日/非工作日
                }else{
                    billingRules.peakModel.isWorkingDay = false;
                }
            }
            if(billingRules.BilllingModel == 1){        //如果是按次收费，直接返回的是封顶金额
                parkingAmount = billingRules.CappingAmount + amount1;
            }else if(billingRules.BilllingModel == 2){  //标准模式收费

                if (overTime%billingRules.commonModel.IncreasingTime > 0) {
                    parkingAmount += billingRules.commonModel.IncreasingAmount;
                }
                parkingAmount += (overTime / billingRules.commonModel.IncreasingTime) * billingRules.commonModel.IncreasingAmount;
                parkingAmount = (parkingAmount >= billingRules.CappingAmount) ? billingRules.CappingAmount : parkingAmount;
                parkingAmount += amount1;

            }else if(billingRules.BilllingModel == 3){      //高峰+工作日模式

                if (billingRules.peakModel.isWorkingDay) {
                    //工作日入场
                    int currentHour = getPrefixHour(entryTime);
                    int currentMin = getPrefixMin(entryTime);
                    do {
                        if (isBetweenPeakMin((currentHour * 60 + currentMin), billingRules.peakModel.PeakTimeStart * 60, billingRules.peakModel.PeakTimeEnd * 60)) {
                            //从非高峰期内进入高峰时期内
                            currentHour += billingRules.peakModel.peak.IncreasingTime / 60;
                            currentMin += billingRules.peakModel.peak.IncreasingTime % 60;
                            overTime -= billingRules.peakModel.peak.IncreasingTime;          //每次都需要把递增过后的剩余时间给计算出来
                            parkingAmount += billingRules.peakModel.peak.IncreasingAmount;
                        } else {
                            //从高峰期内进入非高峰时期内
                            currentHour += billingRules.peakModel.nonPeak.IncreasingTime / 60;
                            currentMin += billingRules.peakModel.nonPeak.IncreasingTime % 60;
                            overTime -= billingRules.peakModel.nonPeak.IncreasingTime;          //每次都需要把递增过后的剩余时间给计算出来
                            parkingAmount += billingRules.peakModel.nonPeak.IncreasingAmount;
                            currentHour = (currentHour >= 24) ? 0 : currentHour;
                        }
                    }while(overTime > 0);
                    parkingAmount = (parkingAmount >= billingRules.CappingAmount) ? billingRules.CappingAmount : parkingAmount;
                    parkingAmount += amount1;
                } else {    //非工作日
                    if (overTime%billingRules.peakModel.nonWorkingDay.IncreasingTime > 0) {
                        parkingAmount += billingRules.peakModel.nonWorkingDay.IncreasingAmount;
                    }
                    parkingAmount += (overTime/billingRules.peakModel.nonWorkingDay.IncreasingTime) * billingRules.peakModel.nonWorkingDay.IncreasingAmount;
                    parkingAmount = (parkingAmount >= billingRules.peakModel.noWorkDayCappingAmount) ? billingRules.peakModel.noWorkDayCappingAmount : parkingAmount;
                    parkingAmount += amount1;
                }

            }else if(billingRules.BilllingModel == 4){      //白天晚上模式
                //第一步看是否满足免费15分钟条件
                if (overTime <= billingRules.FreeTime){
                    parkingAmount = 0 + amount1;              //免费时间段
                }else {
                        //晚上开始入场
                        if (overTime <= billingRules.dayNightModel.night.FirstTime) {
                            parkingAmount = billingRules.dayNightModel.night.FirstAmount + amount1;
                        } else {
                            int tempTime = overTime - billingRules.dayNightModel.night.FirstTime;
                            parkingAmount = billingRules.dayNightModel.night.FirstAmount;
                            int currentHour = 0 + billingRules.dayNightModel.night.FirstTime/60;
                            int currentMin = 0 + billingRules.dayNightModel.night.FirstTime%60;

                            do {
                                if (isBetweenPeakMin((currentHour * 60 + currentMin), billingRules.dayNightModel.DayTimeStart * 60, billingRules.dayNightModel.DayTimeEnd * 60)) {
                                    //从晚上进入白天时期内
                                    currentHour += billingRules.dayNightModel.day.IncreasingTime / 60;
                                    currentMin += billingRules.dayNightModel.day.IncreasingTime % 60;
                                    tempTime -= billingRules.dayNightModel.day.IncreasingTime;          //每次都需要把递增过后的剩余时间给计算出来
                                    parkingAmount += billingRules.dayNightModel.day.IncreasingAmount;

                                } else {
                                    //从白天进入晚上时期内
                                    currentHour += billingRules.dayNightModel.night.IncreasingTime / 60;
                                    currentMin += billingRules.dayNightModel.night.IncreasingTime % 60;
                                    tempTime -= billingRules.dayNightModel.night.IncreasingTime;          //每次都需要把递增过后的剩余时间给计算出来
                                    parkingAmount += billingRules.dayNightModel.night.IncreasingAmount;
                                }
                            }while(tempTime > 0);
                            parkingAmount = (parkingAmount >= billingRules.CappingAmount) ? billingRules.CappingAmount : parkingAmount;
                            parkingAmount += amount1;
                        }
                }
            }

            parkingAmount += currentDayAmount;
        }

        //计费规则暂定,先固定传入1用来测试
        return parkingAmount;
    }

    //这里仅为计算出截止到当天的费用
    private double getCurrentDayEndParkingCost(int plateColor, String entryTime, int parkingHour, int parkMin){

        double parkingAmount = 0;
//        //第一步，先将临界点的天数给确定出来
            //没有超过临界点的天数
            if(billingRules.BilllingModel == 1){        //如果是按次收费，直接返回的是封顶金额
                if (parkingHour * 60 + parkMin <= billingRules.FreeTime) {
                    parkingAmount = 0;                      //免费时间段
                }else {
                    parkingAmount = billingRules.CappingAmount;
                }
            }else if(billingRules.BilllingModel == 2){  //标准模式收费
                if (parkingHour * 60 + parkMin <= billingRules.FreeTime) {
                    parkingAmount = 0;                      //免费时间段
                } else {
                    if (parkingHour * 60 + parkMin <= billingRules.commonModel.FirstTime) {
                        parkingAmount = billingRules.commonModel.FirstAmount;
                    } else {
                        int tempTime = parkingHour * 60 + parkMin - billingRules.commonModel.FirstTime;
                        parkingAmount = billingRules.commonModel.FirstAmount;
                        if (tempTime % billingRules.commonModel.IncreasingTime > 0) {
                            parkingAmount += billingRules.commonModel.IncreasingAmount;
                        }
                        parkingAmount += (tempTime / billingRules.commonModel.IncreasingTime) * billingRules.commonModel.IncreasingAmount;
                    }
                }
                parkingAmount = (parkingAmount >= billingRules.CappingAmount) ? billingRules.CappingAmount : parkingAmount;
            }else if(billingRules.BilllingModel == 3){      //高峰+工作日模式
                //第一步看是否满足免费15分钟条件
                if (parkingHour * 60 + parkMin <= billingRules.FreeTime){
                    parkingAmount = 0;                      //免费时间段
                }else {
                    if(isWeekDayFromEntryTime(entryTime)){
                        billingRules.peakModel.isWorkingDay = false;
                    }
                    //第二步先判断入场的时间是不是在工作日的高峰时期内
                    parkingAmount = getPeakModelHourAmount(entryTime, parkingHour, parkMin, 0);
                }
            }else if(billingRules.BilllingModel == 4){      //白天晚上的收费模式
                //第一步看是否满足免费15分钟条件
                if (parkingHour * 60 + parkMin <= billingRules.FreeTime){
                    parkingAmount = 0;                      //免费时间段
                }else {
                    //第二步先判断入场的时间是不是在白天
                        if ((getPrefixHour(entryTime) < billingRules.dayNightModel.DayTimeStart) ||
                                (getPrefixHour(entryTime) >= billingRules.dayNightModel.DayTimeEnd)) {
                            //晚上入场
                            if (parkingHour * 60 + parkMin <= billingRules.dayNightModel.night.FirstTime) {
                                parkingAmount = billingRules.dayNightModel.night.FirstAmount;
                            } else {
                                int tempTime = parkingHour * 60 + parkMin - billingRules.dayNightModel.night.FirstTime;
                                parkingAmount = billingRules.dayNightModel.night.FirstAmount;
                                int currentHour = getPrefixHour(entryTime) + billingRules.dayNightModel.night.FirstTime/60;
                                int currentMin = getPrefixMin(entryTime) + billingRules.dayNightModel.night.FirstTime%60;

                                do {
                                    if (isBetweenPeakMin((currentHour * 60 + currentMin), billingRules.dayNightModel.DayTimeStart * 60, billingRules.dayNightModel.DayTimeEnd * 60)) {
                                        //从晚上进入白天时期内
                                        currentHour += billingRules.dayNightModel.day.IncreasingTime / 60;
                                        currentMin += billingRules.dayNightModel.day.IncreasingTime % 60;
                                        tempTime -= billingRules.dayNightModel.day.IncreasingTime;          //每次都需要把递增过后的剩余时间给计算出来
                                        parkingAmount += billingRules.dayNightModel.day.IncreasingAmount;
                                    } else {
                                        //从白天进入晚上时期内
                                        currentHour += billingRules.dayNightModel.night.IncreasingTime / 60;
                                        currentMin += billingRules.dayNightModel.night.IncreasingTime % 60;
                                        tempTime -= billingRules.dayNightModel.night.IncreasingTime;          //每次都需要把递增过后的剩余时间给计算出来
                                        parkingAmount += billingRules.dayNightModel.night.IncreasingAmount;
                                    }
                                }while(tempTime > 0);
                            }

                        } else {
                            //白天入场
                            if (parkingHour * 60 + parkMin <= billingRules.dayNightModel.day.FirstTime) {
                                parkingAmount = billingRules.dayNightModel.day.FirstAmount;
                            } else {
                                int tempTime = parkingHour * 60 + parkMin - billingRules.dayNightModel.day.FirstTime;
                                parkingAmount = billingRules.dayNightModel.day.FirstAmount;
                                int currentHour = getPrefixHour(entryTime) + billingRules.dayNightModel.day.FirstTime/60;
                                int currentMin = getPrefixMin(entryTime) + billingRules.dayNightModel.day.FirstTime%60;

                                do {
                                    if (isBetweenPeakMin((currentHour * 60 + currentMin), billingRules.dayNightModel.DayTimeStart * 60, billingRules.dayNightModel.DayTimeEnd * 60)) {
                                        // 白天时期内
                                        currentHour += billingRules.dayNightModel.day.IncreasingTime / 60;
                                        currentMin += billingRules.dayNightModel.day.IncreasingTime % 60;
                                        tempTime -= billingRules.dayNightModel.day.IncreasingTime;          //每次都需要把递增过后的剩余时间给计算出来
                                        parkingAmount += billingRules.dayNightModel.day.IncreasingAmount;

                                    } else {
                                        //从白天进入晚上时期内
                                        currentHour += billingRules.dayNightModel.night.IncreasingTime / 60;
                                        currentMin += billingRules.dayNightModel.night.IncreasingTime % 60;
                                        tempTime -= billingRules.dayNightModel.night.IncreasingTime;          //每次都需要把递增过后的剩余时间给计算出来
                                        parkingAmount += billingRules.dayNightModel.night.IncreasingAmount;
                                    }
                                }while(tempTime > 0);
                            }
                        }
                    parkingAmount = (parkingAmount >= billingRules.CappingAmount) ? billingRules.CappingAmount : parkingAmount;
                }
            }

        return parkingAmount;
    }


    /**
     * 计算出当前整个停车天数下面的封顶金额
     * @param entryTime
     * @return
     */
    private double getCappingAmountByAllDays(String entryTime, int parkingDays){

        double dayCappingAount = 0;
        //这里计算出入场的时候是周几
        int dayOfWeek = getWhichDayFromEntryTime(entryTime);
        if(billingRules.BilllingModel == 3) {       //这里只有工作日+高峰期模式才有2个不同的封顶金额
            for (int i = 1; i <= parkingDays; i++) {
                if (((dayOfWeek + i) % 7) >= 1 && ((dayOfWeek + i) % 7) < 6) {
                    //工作日
                    billingRules.peakModel.isWorkingDay = true;
                    double temmp24HourAmount = getPeakModelHourAmount(entryTime, 24, 0, 1);
                    double currentDayAmount = (temmp24HourAmount > billingRules.CappingAmount) ? billingRules.CappingAmount : temmp24HourAmount;
                    dayCappingAount += currentDayAmount;
                } else {
                    //非工作日
                    billingRules.peakModel.isWorkingDay = false;
                    double temmp24HourAmount = getPeakModelHourAmount(entryTime, 24, 0, 1);
                    double currentDayAmount = (temmp24HourAmount > billingRules.peakModel.noWorkDayCappingAmount) ? billingRules.peakModel.noWorkDayCappingAmount : temmp24HourAmount;
                    dayCappingAount += currentDayAmount;
                }
            }
        }else if(billingRules.BilllingModel == 2){
            double temmp24HourAmount = ((24*60) / billingRules.commonModel.IncreasingTime) * billingRules.commonModel.IncreasingAmount;
            double currentDayAmount = (temmp24HourAmount >= billingRules.CappingAmount) ? billingRules.CappingAmount : temmp24HourAmount;
            dayCappingAount = parkingDays*currentDayAmount;
        }else if(billingRules.BilllingModel == 1){
            dayCappingAount = parkingDays*billingRules.CappingAmount;
        }
        return dayCappingAount;
    }

    private double getPeakModelHourAmount(String entryTime, int parkingHour, int parkMin, int type){

        double parkingAmount = 0;
        if (billingRules.peakModel.isWorkingDay) {        //先默认设定为工作日
            if ((getPrefixHour(entryTime) < billingRules.peakModel.PeakTimeStart) ||
                    (getPrefixHour(entryTime) >= billingRules.peakModel.PeakTimeEnd)) {
                //工作日非高峰期入场
                if (parkingHour * 60 + parkMin <= billingRules.peakModel.nonPeak.FirstTime) {
                    parkingAmount = billingRules.peakModel.nonPeak.FirstAmount;
                } else {
                    int tempTime = parkingHour *60 + parkMin;
                    if(type == 0){
                        tempTime = parkingHour * 60 + parkMin - billingRules.peakModel.nonPeak.FirstTime;
                        parkingAmount = billingRules.peakModel.nonPeak.FirstAmount;
                    }
                    int currentHour = getPrefixHour(entryTime) + billingRules.peakModel.nonPeak.FirstTime/60;
                    int currentMin = getPrefixMin(entryTime) + billingRules.peakModel.nonPeak.FirstTime%60;

                    do {
                        if (isBetweenPeakMin((currentHour * 60 + currentMin), billingRules.peakModel.PeakTimeStart * 60, billingRules.peakModel.PeakTimeEnd * 60)) {
                            //从非高峰期内进入高峰时期内
                            currentHour += billingRules.peakModel.peak.IncreasingTime / 60;
                            currentMin += billingRules.peakModel.peak.IncreasingTime % 60;
                            tempTime -= billingRules.peakModel.peak.IncreasingTime;          //每次都需要把递增过后的剩余时间给计算出来
                            parkingAmount += billingRules.peakModel.peak.IncreasingAmount;

                        } else {
                            //从高峰期内进入非高峰时期内
                            currentHour += billingRules.peakModel.nonPeak.IncreasingTime / 60;
                            currentMin += billingRules.peakModel.nonPeak.IncreasingTime % 60;
                            tempTime -= billingRules.peakModel.nonPeak.IncreasingTime;          //每次都需要把递增过后的剩余时间给计算出来
                            parkingAmount += billingRules.peakModel.nonPeak.IncreasingAmount;
                            currentHour = (currentHour >= 24) ? 0 : currentHour;
                        }
                    }while(tempTime > 0);
                }
            } else {
                //工作日高峰期入场
                if (parkingHour * 60 + parkMin <= billingRules.peakModel.peak.FirstTime) {
                    parkingAmount = billingRules.peakModel.peak.FirstAmount;
                } else {
                    int tempTime = parkingHour * 60 + parkMin;
                    if(type == 0){
                        tempTime = parkingHour * 60 + parkMin - billingRules.peakModel.peak.FirstTime;
                        parkingAmount = billingRules.peakModel.peak.FirstAmount;
                    }
                    int currentHour = getPrefixHour(entryTime) + billingRules.peakModel.peak.FirstTime/60;
                    int currentMin = getPrefixMin(entryTime) + billingRules.peakModel.peak.FirstTime%60;

                    do {
                        if (isBetweenPeakMin((currentHour * 60 + currentMin), billingRules.peakModel.PeakTimeStart * 60, billingRules.peakModel.PeakTimeEnd * 60)) {
                            //高峰时期内
                            currentHour += billingRules.peakModel.peak.IncreasingTime / 60;
                            currentMin += billingRules.peakModel.peak.IncreasingTime % 60;
                            tempTime -= billingRules.peakModel.peak.IncreasingTime;          //每次都需要把递增过后的剩余时间给计算出来
                            parkingAmount += billingRules.peakModel.peak.IncreasingAmount;

                        } else {
                            //从高峰期内进入非高峰时期内
                            currentHour += billingRules.peakModel.nonPeak.IncreasingTime / 60;
                            currentMin += billingRules.peakModel.nonPeak.IncreasingTime % 60;
                            tempTime -= billingRules.peakModel.nonPeak.IncreasingTime;          //每次都需要把递增过后的剩余时间给计算出来
                            parkingAmount += billingRules.peakModel.nonPeak.IncreasingAmount;
                            currentHour = (currentHour >= 24) ? 0 : currentHour;
                        }
                    }while(tempTime > 0);
                }
            }
            parkingAmount = (parkingAmount >= billingRules.CappingAmount) ? billingRules.CappingAmount : parkingAmount;
        } else {                                          //非工作日
            if (parkingHour * 60 + parkMin <= billingRules.peakModel.nonWorkingDay.FirstTime) {
                parkingAmount = billingRules.peakModel.nonWorkingDay.FirstAmount;
            } else {
                int tempTime = parkingHour * 60 + parkMin;
                if(type == 0){
                    tempTime = parkingHour * 60 + parkMin - billingRules.peakModel.nonWorkingDay.FirstTime;
                    parkingAmount = billingRules.peakModel.nonWorkingDay.FirstAmount;
                }
                if (tempTime % billingRules.peakModel.nonWorkingDay.IncreasingTime > 0) {
                    parkingAmount += billingRules.peakModel.nonWorkingDay.IncreasingAmount;
                }
                parkingAmount += (tempTime / billingRules.peakModel.nonWorkingDay.IncreasingTime) * billingRules.peakModel.nonWorkingDay.IncreasingAmount;

            }
            parkingAmount = (parkingAmount >= billingRules.peakModel.noWorkDayCappingAmount) ? billingRules.peakModel.noWorkDayCappingAmount : parkingAmount;
        }
        return parkingAmount;
    }


    private String getDayEndTime(String entryTime){

        int index = entryTime.indexOf(" ");
        String time = entryTime.substring(0, index+1);
        time += "23:59:59";

        return time;
    }

    //获取前缀的小时
    private int getPrefixHour(String time){

        int index = time.indexOf(" ");
        String suffix = time.substring(index+1);    //23:59:59

        return Integer.parseInt(getRegexString(suffix, ":", 1));
    }

    //获取前缀的分钟
    private int getPrefixMin(String time){

        int index = time.indexOf(" ");
        String suffix = time.substring(index+1);    //23:59:59

        return Integer.parseInt(getRegexString(suffix, ":", 2));
    }

    //判断当前的时间是否在高峰期内  true: 高峰期 false: 非高峰期
    private boolean isBetweenPeakMin(int currentHour, int peakStartTimeHour, int peakEndTimeHour){
        if(currentHour >= peakStartTimeHour && currentHour < peakEndTimeHour){
            return true;
        }
        return false;
    }

    //判断当前日期是否为周末,周末为1,周六为7
    private boolean isWeekDay(){
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay) || "7".equals(mWay)){
            return true;
        }
        return false;
    }

    //根据入场的时间来判断当前日期是否为周末,周末为1,周六为7  时间格式为 yy/MM/dd HH:mm:ss
    private boolean isWeekDayFromEntryTime(String entryTime){
        try{
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date =sdf.parse(entryTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String mWay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
            if("1".equals(mWay) || "7".equals(mWay)){
                return true;
            }
        }catch (Exception e){

        }
        return false;
    }

    //根据入场的时间来判断当前为星期几， 周末为7,周一为1 时间格式为 yy/MM/dd HH:mm:ss
    private int getWhichDayFromEntryTime(String entryTime){
        int dayOfWeek = 0;
        try{
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date =sdf.parse(entryTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
            if(dayOfWeek == 0){
                dayOfWeek = 7;
            }
        }catch (Exception e){
        }
        return dayOfWeek;
    }

    public String getRegexString(String srcStr, String regex, int index){
        if(!srcStr.contains(regex)){
            throw  new IllegalArgumentException("regex is error");
        }
        Pattern pat= Pattern.compile(regex);
        String[] urlStr = pat.split(srcStr);
        if((urlStr.length < index && index != 100) || index <= 0){
            throw  new IllegalArgumentException("index is error");
        }
        if(index == 100){
            return urlStr[urlStr.length-1];
        }else{
            return urlStr[index-1];
        }
    }

}
