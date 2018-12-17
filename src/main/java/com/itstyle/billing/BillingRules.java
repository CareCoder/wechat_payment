package com.itstyle.billing;

public class BillingRules {


    protected int BilllingModel;                          //计费模式  1，按次付费 2，标准收费 3，高峰+工作日 4,白天晚上模式
    protected int CappingAmount;                          //封顶金额
    protected int FreeTime;                               //免费时间
    protected BillingCommonStandard commonModel;          //标准收费模式变量
    protected BillingPeakModel peakModel;                 //高峰+工作日收费模式变量
    protected BillingDayNightModel dayNightModel;         //白天黑夜时间模式

    private double firstAmount;                               //首段金额


    public BillingRules(int BilllingModel, int CappingAmount, int FreeTime, int LC_COLOR){

        this.BilllingModel = BilllingModel;
        this.CappingAmount = CappingAmount;
        this.FreeTime = FreeTime;
        if(BilllingModel == 1){                         //按次收费
            commonModel = new BillingCommonStandard();
            if(LC_COLOR == BilllingCost.LC_BLUE) {
                this.firstAmount = CappingAmount;
            }else if(LC_COLOR == BilllingCost.LC_YELLOW){

            }else if(LC_COLOR == BilllingCost.LC_BLACK){

            }else if(LC_COLOR == BilllingCost.LC_GREEN){

            }
        }else if(BilllingModel == 2){
            commonModel = new BillingCommonStandard();
            if(LC_COLOR == BilllingCost.LC_BLUE) {
//                commonModel.FirstTime = 60;                 //首段60分钟收费5元，以后每60分钟收费1元
//                commonModel.FirstAmount = 5;
//                commonModel.IncreasingTime = 60;
//                commonModel.IncreasingAmount = 1;
            }else if(LC_COLOR == BilllingCost.LC_YELLOW){

            }else if(LC_COLOR == BilllingCost.LC_BLACK){

            }else if(LC_COLOR == BilllingCost.LC_GREEN){

            }
        }else if(BilllingModel == 3){
            peakModel = new BillingPeakModel();
            peakModel.peak = new BillingCommonStandard();
            peakModel.nonPeak = new BillingCommonStandard();
            peakModel.nonWorkingDay = new BillingCommonStandard();
            peakModel.isWorkingDay = true;              //先默认设定为工作日
            if(LC_COLOR == BilllingCost.LC_BLUE) {
//                peakModel.PeakTimeStart = 8;                //高峰期+工作日模式（8：00-20：00） 免费15分钟
//                peakModel.PeakTimeEnd = 20;
//                //
//                peakModel.peak.FirstTime = 60;                 //高峰期：首段60分钟收费15元，每30分钟1.5元
//                peakModel.peak.FirstAmount = 15;
//                peakModel.peak.IncreasingTime = 30;
//                peakModel.peak.IncreasingAmount = 1.5;
//                //
//                peakModel.nonPeak.FirstTime = 60;                 //非高峰期：首段60分钟收费1元，以后每60分钟收费1元
//                peakModel.nonPeak.FirstAmount = 1;
//                peakModel.nonPeak.IncreasingTime = 60;
//                peakModel.nonPeak.IncreasingAmount = 1;
//                //
//                peakModel.nonWorkingDay.FirstTime = 60;                 //非工作日：首段60分钟收费5元，以后每60分钟收费1元
//                peakModel.nonWorkingDay.FirstAmount = 5;
//                peakModel.nonWorkingDay.IncreasingTime = 60;
//                peakModel.nonWorkingDay.IncreasingAmount = 1;
            }else if(LC_COLOR == BilllingCost.LC_YELLOW){

            }else if(LC_COLOR == BilllingCost.LC_BLACK){

            }else if(LC_COLOR == BilllingCost.LC_GREEN){

            }
        }else if(BilllingModel == 4){                   //白天夜间收费标准
            dayNightModel = new BillingDayNightModel();
            dayNightModel.day = new BillingCommonStandard();
            dayNightModel.night = new BillingCommonStandard();
            if(LC_COLOR == BilllingCost.LC_BLUE) {
//                dayNightModel.DayTimeStart = 6;                //白天开始时间（6：00-20：00） 免费15分钟,剩余的时间则是晚上
//                dayNightModel.DayTimeEnd = 20;
//                dayNightModel.DayCappingAmount = 20;            //白天封顶金额
//                dayNightModel.NightCappingAmount = 10;          //晚上封顶金额
//                //
//                dayNightModel.day.FirstTime = 60;                 //首段60分钟收费15元, 白天每30分钟1.5元，这里没有首段时间的说法
//                dayNightModel.day.FirstAmount = 15;
//                dayNightModel.day.IncreasingTime = 30;
//                dayNightModel.day.IncreasingAmount = 1.5;
//                //
//                dayNightModel.night.FirstTime = 60;                 //首段60分钟收费5元, 晚上每60分钟收费1元
//                dayNightModel.night.FirstAmount = 5;
//                dayNightModel.night.IncreasingTime = 60;
//                dayNightModel.night.IncreasingAmount = 1;

            }else if(LC_COLOR == BilllingCost.LC_YELLOW){

            }else if(LC_COLOR == BilllingCost.LC_BLACK){

            }else if(LC_COLOR == BilllingCost.LC_GREEN){

            }
        }
    }
///////////////////////标准收费
    /**
     * --- 标准计费模式初始化数据        首次时间-------首次金额-----------------递增时间---------------递增金额
     */
    public void CommonStandardInit(int firstTime, double firstAmount, int increasingTime, double increasingAmount, int optionalCycle){

        commonModel.FirstTime = firstTime;                 //首段60分钟收费5元，以后每60分钟收费1元
        commonModel.FirstAmount = firstAmount;
        commonModel.IncreasingTime = increasingTime;
        commonModel.IncreasingAmount = increasingAmount;
        commonModel.optionalCycle = optionalCycle;
        this.firstAmount = firstAmount;
    }

///////////////////////高峰+工作日
    /**
     * --- 高峰+工作日模式初始化高峰时间段       工作日高峰期开始时间-------工作日高峰期结束时间
     */
    public void PeakModelPeakTimeSlot(int PeakTimeStart, int PeakTimeEnd, int noWorkDayCappingAmount, int optionalCycle){

        peakModel.PeakTimeStart = PeakTimeStart;                //高峰期+工作日模式（8：00-20：00）
        peakModel.PeakTimeEnd = PeakTimeEnd;
        peakModel.noWorkDayCappingAmount = noWorkDayCappingAmount;
        peakModel.optionalCycle = optionalCycle;
    }

    /**
     * --- 高峰+工作日模式初始化_高峰   首次时间-------首次金额-----------------递增时间---------------递增金额
     */
    public void PeakModel_peak(int firstTime, double firstAmount, int increasingTime, double increasingAmount){

        peakModel.peak.FirstTime = firstTime;
        peakModel.peak.FirstAmount = firstAmount;
        peakModel.peak.IncreasingTime = increasingTime;
        peakModel.peak.IncreasingAmount = increasingAmount;
    }

    /**
     * --- 高峰+工作日模式初始化_非高峰   首次时间-------首次金额-----------------递增时间---------------递增金额
     */
    public void PeakModel_nonPeak(int firstTime, double firstAmount, int increasingTime, double increasingAmount){

        peakModel.nonPeak.FirstTime = firstTime;
        peakModel.nonPeak.FirstAmount = firstAmount;
        peakModel.nonPeak.IncreasingTime = increasingTime;
        peakModel.nonPeak.IncreasingAmount = increasingAmount;
        this.firstAmount = firstAmount;
    }

    /**
     * --- 高峰+工作日模式初始化_非工作日   首次时间-------首次金额-----------------递增时间---------------递增金额
     */
    public void PeakModel_nonWorkingDay(int firstTime, double firstAmount, int increasingTime, double increasingAmount){

        peakModel.nonWorkingDay.FirstTime = firstTime;
        peakModel.nonWorkingDay.FirstAmount = firstAmount;
        peakModel.nonWorkingDay.IncreasingTime = increasingTime;
        peakModel.nonWorkingDay.IncreasingAmount = increasingAmount;
    }
///////////////////////白天晚上收费模式
    /**
     * --- 白天晚上模式初始化高峰时间段       工作日高峰期开始时间-------工作日高峰期结束时间
     */
    public void DayNightModelDayTimeSlot(int DayTimeStart, int DayTimeEnd, int DayCappingAmount, int NightCappingAmount){

        dayNightModel.DayTimeStart = DayTimeStart;                //白天开始时间（6：00-20：00） 免费15分钟,剩余的时间则是晚上
        dayNightModel.DayTimeEnd = DayTimeEnd;
        dayNightModel.DayCappingAmount = DayCappingAmount;            //白天封顶金额
        dayNightModel.NightCappingAmount = NightCappingAmount;          //晚上封顶金额
    }
    /**
     * --- 白天晚上模式初始化_白天   首次时间-------首次金额-----------------递增时间---------------递增金额
     */
    public void DayNightModel_day(int firstTime, double firstAmount, int increasingTime, double increasingAmount){

        dayNightModel.day.FirstTime = firstTime;
        dayNightModel.day.FirstAmount = firstAmount;
        dayNightModel.day.IncreasingTime = increasingTime;
        dayNightModel.day.IncreasingAmount = increasingAmount;
    }
    /**
     * --- 白天晚上模式初始化_晚上   首次时间-------首次金额-----------------递增时间---------------递增金额
     */
    public void DayNightModel_night(int firstTime, double firstAmount, int increasingTime, double increasingAmount){

        dayNightModel.night.FirstTime = firstTime;
        dayNightModel.night.FirstAmount = firstAmount;
        dayNightModel.night.IncreasingTime = increasingTime;
        dayNightModel.night.IncreasingAmount = increasingAmount;
    }

    /**
     *
     * @return  获取首段金额
     */
    public double GetFirstAmount(){
        return this.firstAmount;
    }



    public class BillingPeakModel{

        boolean isWorkingDay;                   //是否是工作日
        int PeakTimeStart;                      //高峰开始时间
        int PeakTimeEnd;                        //高峰结束时间
        int optionalCycle;                      //可选周期
        int noWorkDayCappingAmount;             //非工作日的封顶金额
        BillingCommonStandard peak;             //高峰时间段的计费标准
        BillingCommonStandard nonPeak;          //非高峰时间段的计费标准
        BillingCommonStandard nonWorkingDay;    //非工作日的计费标准
    }

    public class BillingDayNightModel{

        int DayTimeStart;                      //白天开始时间
        int DayTimeEnd;                        //白天结束时间
        int DayCappingAmount;               //白天封顶金额
        int NightCappingAmount;             //晚上封顶金额
        BillingCommonStandard day;             //白天的收费标准
        BillingCommonStandard night;           //晚上的收费标准
    }


    public class BillingCommonStandard{         //通用的计费标准

        int FirstTime;                          //首次时间   首段60分钟收费5元，以后每60分钟收费1元
        double FirstAmount;                        //首次金额
        int IncreasingTime;                     // 递增时间
        double IncreasingAmount;                   //递增金额
        int optionalCycle;                      //可选一天or24小时，周期后就要启用首段时间 0: 一天，1：24小时

    }








}
