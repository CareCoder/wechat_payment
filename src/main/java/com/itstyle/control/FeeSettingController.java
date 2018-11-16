package com.itstyle.control;

import com.google.gson.Gson;
import com.itstyle.common.YstCommon;
import com.itstyle.dao.RedisDao;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.feesettings.ByCharges;
import com.itstyle.domain.feesettings.SZCharges;
import com.itstyle.domain.feesettings.StandardCharges;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;
import com.itstyle.utils.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fee")
public class FeeSettingController {

    private RedisDao redisDao;
    private Gson gson;

    @Autowired
    public FeeSettingController(RedisDao redisDao, Gson gson) {
        this.redisDao = redisDao;
        this.gson = gson;
    }

    @PostMapping("/sz/save")
    @ResponseBody
    public Response saveSZCharges(SZCharges szCharges) {
        AssertUtil.assertNotNull(szCharges, () -> new BusinessException("sz charges is null"));
        AssertUtil.assertNotEmpty(szCharges.getCarType(), () -> new BusinessException("车辆类型不能为空"));
        redisDao.hset(YstCommon.SZ_CHARGES, szCharges.getCarType(), gson.toJson(szCharges));
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/sz/get/{carType}")
    @ResponseBody
    public Response getSZCharges(@PathVariable("carType") String carType) {
        AssertUtil.assertNotEmpty(carType, () -> new BusinessException("车辆类型不能为空"));
        String result = redisDao.hget(YstCommon.SZ_CHARGES, carType);
        SZCharges szCharges = gson.fromJson(result, SZCharges.class);
        return Response.build(Status.NORMAL, null, szCharges);
    }

    @GetMapping("/sz/page")
    public String getSZChargesPage(Model model) {
        String szCharges = redisDao.hget(YstCommon.SZ_CHARGES, CarType.TEMP_CAR_A.name());
        model.addAttribute("sz_charges", gson.fromJson(szCharges, SZCharges.class));
        return "/backend/sz-charges";
    }

    @PostMapping("/standard/save")
    @ResponseBody
    public Response saveStandardCharges(StandardCharges standardCharges) {
        AssertUtil.assertNotNull(standardCharges, () -> new BusinessException("standard charges is null"));
        AssertUtil.assertNotEmpty(standardCharges.getCarType(), () -> new BusinessException("车辆类型不能为空"));
        redisDao.hset(YstCommon.STANDARD_CHARGES, standardCharges.getCarType(), gson.toJson(standardCharges));
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/standard/get/{carType}")
    @ResponseBody
    public Response getStandardCharges(@PathVariable("carType") String carType) {
        AssertUtil.assertNotEmpty(carType, () -> new BusinessException("车辆类型不能为空"));
        String result = redisDao.hget(YstCommon.STANDARD_CHARGES, carType);
        return Response.build(Status.NORMAL, null, gson.fromJson(result, StandardCharges.class));
    }

    @GetMapping("/standard/page")
    public String getStandardChargesPage(Model model) {
        String standardCharges = redisDao.hget(YstCommon.STANDARD_CHARGES, CarType.TEMP_CAR_A.name());
        model.addAttribute("standard_charges", gson.fromJson(standardCharges, StandardCharges.class));
        return "/backend/standard-charges";
    }

    @PostMapping("/by/save")
    @ResponseBody
    public Response saveByCharges(ByCharges byCharges) {
        AssertUtil.assertNotNull(byCharges, () -> new BusinessException("by charges is null"));
        AssertUtil.assertNotNull(byCharges.getCarType(), () -> new BusinessException("车辆类型不能为空"));
        redisDao.hset(YstCommon.BY_CHARGES, byCharges.getCarType(), gson.toJson(byCharges));
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/by/get/{carType}")
    @ResponseBody
    public Response getByCharges(@PathVariable("carType") String carType) {
        AssertUtil.assertNotEmpty(carType, () -> new BusinessException("车辆类型不能为空"));
        String result = redisDao.hget(YstCommon.BY_CHARGES, carType);
        return Response.build(Status.NORMAL, null, gson.fromJson(result, ByCharges.class));
    }

    @GetMapping("/by/page")
    public String getByChargesPage(Model model) {
        String byCharges = redisDao.hget(YstCommon.BY_CHARGES, CarType.TEMP_CAR_A.name());
        model.addAttribute("by_charges", gson.fromJson(byCharges, ByCharges.class));
        return "/backend/by-charges";
    }

}
