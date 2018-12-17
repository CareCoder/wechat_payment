package com.itstyle.control;

import com.google.gson.Gson;
import com.itstyle.billing.BilllingCost;
import com.itstyle.common.YstCommon;
import com.itstyle.dao.RedisDao;
import com.itstyle.domain.feesettings.ByCharges;
import com.itstyle.domain.feesettings.SZCharges;
import com.itstyle.domain.feesettings.StandardCharges;
import com.itstyle.domain.feesettings.response.FeeTestResponse;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.utils.enums.Status;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/fee_test")
public class FeeTestController {

    private RedisDao redisDao;
    private Gson gson;

    private Map<Integer, String> mapperCarTyep = new HashMap<>();

    @PostConstruct
    public void initMapper() {
        mapperCarTyep.put(0, "TEMP_CAR_A");
        mapperCarTyep.put(1, "TEMP_CAR_B");
        mapperCarTyep.put(2, "TEMP_CAR_C");
        mapperCarTyep.put(3, "TEMP_CAR_D");
    }

    public FeeTestController(RedisDao redisDao, Gson gson) {
        this.redisDao = redisDao;
        this.gson = gson;
    }

    @GetMapping("/page")
    public String page() {
        return "/backend/fee-test";
    }

    @GetMapping("/get")
    @ResponseBody
    public Response get(Integer chargingStandard, Integer carType, Long start, Long end) {
        if (chargingStandard == 1) {
            return byCharges(carType, start, end);
        }
        if (chargingStandard == 2) {
            return standardCharges(carType, start, end);
        }
        if (chargingStandard == 3) {
            return szCharges(carType, start, end);
        }
        return null;
    }

    private Response byCharges(Integer carType, Long start, Long end) {
        String result = redisDao.hget(YstCommon.BY_CHARGES, mapperCarTyep.get(carType));
        ByCharges byCharges = gson.fromJson(result, ByCharges.class);
        BilllingCost billlingCost = new BilllingCost(1, carType, byCharges.getCappingAmount(), byCharges.getFreeTime());
        billlingCost.getBillingRules().CommonStandardInit(0,
                0, 0, 0, byCharges.getOptionalCycle() == 24 ? 1 : 0);
        return getCalculationResults(start, end, billlingCost, carType);
    }

    private Response standardCharges(Integer carType, Long start, Long end) {
        String result = redisDao.hget(YstCommon.STANDARD_CHARGES, mapperCarTyep.get(carType));
        StandardCharges standardCharges = gson.fromJson(result, StandardCharges.class);
        BilllingCost billlingCost = new BilllingCost(2, carType, standardCharges.getCappingAmount(), standardCharges.getFreeTime());
        billlingCost.getBillingRules().CommonStandardInit(standardCharges.getFirstTime(), standardCharges.getFirstAmount(), (int) (long) standardCharges.getIncreasingTime(),
                standardCharges.getIncreasingAmount(), standardCharges.getOptionalCycle() == 24 ? 1 : 0);
        return getCalculationResults(start, end, billlingCost, carType);
    }

    private Response szCharges(Integer carType, Long start, Long end) {
        String result = redisDao.hget(YstCommon.SZ_CHARGES, mapperCarTyep.get(carType));
        SZCharges szCharges = gson.fromJson(result, SZCharges.class);
        BilllingCost billlingCost = new BilllingCost(3, carType, (int) (long) szCharges.getCappingAmount(), szCharges.getFreeTime());
        billlingCost.getBillingRules().PeakModelPeakTimeSlot(szCharges.getPeakModelPeakTimeStart(), szCharges.getPeakModelPeakTimeEnd(),
                (int) (long) szCharges.getCappingAmount(), szCharges.getOptionalCycle() == 24 ? 1 : 0);
        billlingCost.getBillingRules().PeakModel_peak(szCharges.getPeakFirstTime(), szCharges.getPeakFirstAmount(), szCharges.getPeakIncreasingTime(),
                szCharges.getPeakIncreasingAmount());
        billlingCost.getBillingRules().PeakModel_nonPeak(szCharges.getNonPeakFirstTime(), szCharges.getNonPeakFirstAmount(), szCharges.getNonPeakIncreasingTime(),
                szCharges.getNonPeakIncreasingAmount());
        billlingCost.getBillingRules().PeakModel_nonWorkingDay(szCharges.getNonWorkingDayFirstTime(), szCharges.getNonWorkingDayFirstAmount(),
                szCharges.getNonWorkingDayIncreasingTime(), szCharges.getNonWorkingDayIncreasingAmount());
        return getCalculationResults(start, end, billlingCost, carType);
    }

    private Response getCalculationResults(Long start, Long end, BilllingCost billlingCost, Integer carType) {
        int totalTime = (int) getParkingDuration(start, end);
        int hour = totalTime / 60;
        int min = totalTime % 60;
        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault());
        double parkingCost = billlingCost.ParkingCost(carType, ldt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")), hour, min);
        FeeTestResponse feeTestResponse = new FeeTestResponse();
        feeTestResponse.setParkingTime(hour + "小时" + min + "分钟");
        feeTestResponse.setParkingAmount(parkingCost);
        System.out.println(feeTestResponse);
        return Response.build(Status.NORMAL, null, gson.toJson(feeTestResponse));
    }

    private long getParkingDuration(Long entryTime, Long exitTime){

        long duration = -1;
        try{
            long ms = exitTime - entryTime;
            long days = ms/(1000 * 60 * 60 * 24); //换算成天数
            long hours =(ms - days*(1000 * 60 * 60 * 24))/(1000 * 60 * 60); //换算成小时
            long minutes =(ms - days*(1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60))/(1000 * 60); //换算成分钟
            duration = (days*24 + hours)*60 + minutes;
        }catch (Exception  e){
            e.printStackTrace();
        }
        return duration;
    }

}
