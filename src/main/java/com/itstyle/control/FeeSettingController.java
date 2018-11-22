package com.itstyle.control;

import com.google.gson.Gson;
import com.itstyle.common.YstCommon;
import com.itstyle.dao.RedisDao;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.feesettings.ByCharges;
import com.itstyle.domain.feesettings.SZCharges;
import com.itstyle.domain.feesettings.StandardCharges;
import com.itstyle.domain.feesettings.response.ByChargesResponse;
import com.itstyle.domain.feesettings.response.SZChargesResponse;
import com.itstyle.domain.feesettings.response.StandardChargesResponse;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;
import com.itstyle.utils.BeanUtilIgnore;
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
        SZChargesResponse szChargesResponse = new SZChargesResponse();
        szChargesResponse.setChargeModel(3);
        if (szCharges != null) {
            szChargesResponse.setChargeRule(new SZChargesResponse.ChargeRule());
            BeanUtilIgnore.copyPropertiesIgnoreNull(szCharges, szChargesResponse.getChargeRule());
        }
        return Response.build(Status.NORMAL, null, szChargesResponse);
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
        StandardCharges standardCharges = gson.fromJson(result, StandardCharges.class);
        StandardChargesResponse standardChargesResponse = new StandardChargesResponse();
        standardChargesResponse.setChargeModel(2);
        if (standardCharges != null) {
            standardChargesResponse.setChargeRule(new StandardChargesResponse.ChargeRule());
            BeanUtilIgnore.copyPropertiesIgnoreNull(standardCharges, standardChargesResponse.getChargeRule());
        }
        return Response.build(Status.NORMAL, null, standardChargesResponse);
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
        ByCharges byCharges = gson.fromJson(result, ByCharges.class);
        ByChargesResponse byChargesResponse = new ByChargesResponse();
        byChargesResponse.setChargeModel(1);
        if (byCharges != null) {
            byChargesResponse.setChargeRule(new ByChargesResponse.ChargeRule());
            BeanUtilIgnore.copyPropertiesIgnoreNull(byCharges, byChargesResponse.getChargeRule());
        }
        return Response.build(Status.NORMAL, null, byChargesResponse);
    }

    @GetMapping("/by/page")
    public String getByChargesPage(Model model) {
        String byCharges = redisDao.hget(YstCommon.BY_CHARGES, CarType.TEMP_CAR_A.name());
        model.addAttribute("by_charges", gson.fromJson(byCharges, ByCharges.class));
        return "/backend/by-charges";
    }

    @GetMapping("/current/{current}")
    @ResponseBody
    public Response setCurrentCharges(@PathVariable("current") String current) {
        AssertUtil.assertNotEmpty(current, () -> new BusinessException("当前收费标准不能为空"));
        redisDao.set(YstCommon.CURRENT_CHARGES, current);
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/setting/page")
    public String getSettingPage(Model model) {
        String s = redisDao.get(YstCommon.CURRENT_CHARGES);
        model.addAttribute("charges", s);
        return "/backend/charges-setting";
    }

}
