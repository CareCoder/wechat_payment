package com.itstyle.control;

import com.google.gson.Gson;
import com.itstyle.common.YstCommon;
import com.itstyle.dao.RedisDao;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.caryard.EquipmentStatus;
import com.itstyle.domain.feesettings.response.ByChargesResponse;
import com.itstyle.domain.feesettings.response.SZChargesResponse;
import com.itstyle.domain.feesettings.response.StandardChargesResponse;
import com.itstyle.domain.version.VersionInfo;
import com.itstyle.exception.BusinessException;
import com.itstyle.service.AccountService;
import com.itstyle.service.ExternalInterfaceService;
import com.itstyle.service.FileResourceService;
import com.itstyle.utils.BeanUtilIgnore;
import com.itstyle.utils.enums.Status;
import com.itstyle.vo.charges.reponse.ByChargesResponseVo;
import com.itstyle.vo.charges.reponse.ChargesResponse;
import com.itstyle.vo.charges.reponse.SZChargesResponseVo;
import com.itstyle.vo.charges.reponse.StandardChargesResponseVo;
import com.itstyle.vo.deletevehicleinfo.response.DeleteVehicleInfo;
import com.itstyle.vo.incrementmonly.response.IncrementMonly;
import com.itstyle.vo.inition.response.Inition;
import com.itstyle.vo.login.reponse.LoginResponse;
import com.itstyle.vo.login.request.LoginRequest;
import com.itstyle.vo.phonenumber.response.PhoneNumberList;
import com.itstyle.vo.syncarinfo.response.SynCarInfo;
import com.itstyle.vo.version.request.VersionRequest;
import com.itstyle.vo.version.response.VersionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/external")
public class ExternalInterfaceController {

    private AccountService accountService;
    private RedisDao redisDao;
    private Gson gson;
    private FileResourceService fileResourceService;

    private ExternalInterfaceService externalInterfaceService;


    @Autowired
    public ExternalInterfaceController(AccountService accountService, RedisDao redisDao, Gson gson,
                                       FileResourceService fileResourceServic, ExternalInterfaceService externalInterfaceService) {
        this.accountService = accountService;
        this.redisDao = redisDao;
        this.gson = gson;
        this.fileResourceService = fileResourceServic;
        this.externalInterfaceService = externalInterfaceService;
    }


    /**
     * 外部登陆接口
     * */
    @PostMapping("/login")
    @ResponseBody
    public LoginResponse login(LoginRequest loginRequestVo) {
        log.info("[ExternalInterfaceController] login request param is {}", loginRequestVo);
        LoginResponse responseVo = new LoginResponse();
        responseVo.setServiceCode(loginRequestVo.getServiceCode());
        try {
            accountService.login(loginRequestVo.getUserName(), loginRequestVo.getPassword());
        } catch (BusinessException e) {
            String message = e.getMessage();
            responseVo.setErrorCode(Status.ERROR);
            responseVo.setErrorDesc(message);
        }
        responseVo.setErrorCode(Status.NORMAL);
        return responseVo;
    }

    /**
     * 同步车辆信息配置
     */
    @GetMapping("/synCarInfo")
    @ResponseBody
    public SynCarInfo synCarInfo() {
        return externalInterfaceService.synCarInfo();
    }

     /**
     * 获取全部配置
     */
    @GetMapping("/inition")
    @ResponseBody
    public Inition inition() {
        Inition inition = externalInterfaceService.inition();
        inition.chargeRule = charges();
        return inition;
    }

    /**
     * 获取剩余车位数,这里独立一个接口出来,方便获取数据
     */
    @GetMapping("/restParkNum")
    @ResponseBody
    public Integer restParkNum(){
        return externalInterfaceService.restParkNum();
    }

    /**
     * apk版本更新
     * */
    @PostMapping("/version/update")
    @ResponseBody
    public VersionResponse versionUpdate(VersionRequest versionRequest) {
        log.info("[ExternalInterfaceController] update request param is {}", versionRequest);
        String json = redisDao.get(YstCommon.VERSION_INFO);
        VersionResponse versionResponse = new VersionResponse();
        versionResponse.setServiceCode(versionRequest.getServiceCode());
        if (versionRequest.getVersionCode() == null) {
            versionResponse.setErrorCode(Status.ERROR);
            versionResponse.setErrorDesc("版本号不能为空");
            return versionResponse;
        }
        if (json == null) {
            versionResponse.setErrorCode(Status.ERROR);
            versionResponse.setErrorDesc("未上传apk到服务器，请上传之后下载");
            return versionResponse;
        }
        VersionInfo versionInfo = gson.fromJson(json, VersionInfo.class);
        if (versionRequest.getVersionCode() >= versionInfo.getVersionCode()) {
            versionResponse.setErrorCode(Status.ERROR);
            versionResponse.setErrorDesc("版本号大于或等于服务器当前最近版本");
            return versionResponse;
        }
        versionResponse.setErrorCode(Status.NORMAL);
        versionResponse.setDownloadUrl("/external/version/download/" + versionInfo.getUuid());
        versionResponse.setUpdateContent(versionInfo.getUpdateContent());
        versionResponse.setVersionCode(versionInfo.getVersionCode());
        return versionResponse;
    }

    @GetMapping("/version/download/{uuid}")
    public ResponseEntity<byte[]> download(@PathVariable("uuid") String versionCode) {
        return fileResourceService.downloadByUuid(versionCode);
    }

    /**
     * 同步车辆信息配置
     */
    @GetMapping("/incrementMonly")
    @ResponseBody
    public IncrementMonly incrementMonly(@RequestParam Long startTime,
                                         @RequestParam Long endTime) {
        return externalInterfaceService.incrementMonly(startTime, endTime);
    }

    /**
     * 计费规则
     * */
    @GetMapping("/charges")
    @ResponseBody
    public Object getCharges() {
        return charges();
    }

    private Object charges() {
        String currentCharges = redisDao.get(YstCommon.CURRENT_CHARGES);
        if (currentCharges == null) {
            return null;
        }
        Map<Object, Object> map = redisDao.hgetAll(currentCharges);
        if (YstCommon.SZ_CHARGES.equals(currentCharges)) {
            ChargesResponse<SZChargesResponseVo> c = new ChargesResponse<>();
            c.setChargeModel(3);
            c.setData(map.values().stream().map(o -> gson.fromJson(o.toString(), SZChargesResponse.ChargeRule.class))
                    .map(chargeRule -> {
                        SZChargesResponseVo szChargesResponseVo = new SZChargesResponseVo();
                        BeanUtilIgnore.copyPropertiesIgnoreNull(chargeRule, szChargesResponseVo);
                        CarType carType = Enum.valueOf(CarType.class, chargeRule.getCarType());
                        szChargesResponseVo.setPlateColor(carType.ordinal());
                        return szChargesResponseVo;
                    })
                    .sorted(Comparator.comparing(SZChargesResponseVo::getPlateColor)).collect(Collectors.toList()));
            return c;
        }
        if (YstCommon.STANDARD_CHARGES.equals(currentCharges)) {
            ChargesResponse<StandardChargesResponseVo> c = new ChargesResponse<>();
            c.setChargeModel(2);
            c.setData(map.values().stream().map(o -> gson.fromJson(o.toString(), StandardChargesResponse.ChargeRule.class))
                    .map(chargeRule -> {
                        StandardChargesResponseVo responseVo = new StandardChargesResponseVo();
                        BeanUtilIgnore.copyPropertiesIgnoreNull(chargeRule, responseVo);
                        CarType carType = Enum.valueOf(CarType.class, chargeRule.getCarType());
                        responseVo.setPlateColor(carType.ordinal());
                        return responseVo;
                    })
                    .sorted(Comparator.comparing(StandardChargesResponseVo::getPlateColor)).collect(Collectors.toList()));
            return c;
        }
        if (YstCommon.BY_CHARGES.equals(currentCharges)) {
            ChargesResponse<ByChargesResponseVo> c = new ChargesResponse<>();
            c.setChargeModel(1);
            c.setData(map.values().stream().map(o -> gson.fromJson(o.toString(), ByChargesResponse.ChargeRule.class))
                    .map(chargeRule -> {
                        ByChargesResponseVo byChargesResponseVo = new ByChargesResponseVo();
                        BeanUtilIgnore.copyPropertiesIgnoreNull(chargeRule, byChargesResponseVo);
                        CarType carType = Enum.valueOf(CarType.class, chargeRule.getCarType());
                        byChargesResponseVo.setPlateColor(carType.ordinal());
                        return byChargesResponseVo;
                    })
                    .sorted(Comparator.comparing(ByChargesResponseVo::getPlateColor)).collect(Collectors.toList()));
            return c;
        }
        return "查询无数据";
    }

    @RequestMapping("/getPhoneNumber")
    @ResponseBody
    public PhoneNumberList fetchPhoneNumber() {
        return externalInterfaceService.fetchPhoneNumber();
    }

    @RequestMapping("/deleteVehicleInfo")
    @ResponseBody
    public DeleteVehicleInfo fetchDeleteVehicleInfo(Long startTime, Long endTime) {
        return externalInterfaceService.fetchDeleteVehicleInfo(startTime, endTime);
    }

    @RequestMapping("/uploadEquipmentStatus")
    @ResponseBody
    public void uploadEquipmentStatus(EquipmentStatus equipmentStatus) {
        externalInterfaceService.uploadEquipmentStatus(equipmentStatus);
    }

    @RequestMapping("/changeAmount/QRCode")
    @ResponseBody
    public String createTemporaryQRCode(Integer changeAmount){
        return externalInterfaceService.createTemporaryQRCode(changeAmount);
    }
}
