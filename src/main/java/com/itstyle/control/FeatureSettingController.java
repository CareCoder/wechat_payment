package com.itstyle.control;

import com.itstyle.common.FeatureSettingConfig;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.caryard.FeatureSetting;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.service.GlobalSettingService;
import com.itstyle.utils.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/feature")
public class FeatureSettingController {

    private GlobalSettingService globalSettingService;
    private FeatureSettingConfig featureSettingConfig;

    @Autowired
    public FeatureSettingController(GlobalSettingService globalSettingService,
                                    FeatureSettingConfig featureSettingConfig) {
        this.globalSettingService = globalSettingService;
        this.featureSettingConfig = featureSettingConfig;
    }

    @PostConstruct
    public void init() {
        List<FeatureSetting> o = (List<FeatureSetting>) globalSettingService.get(YstCommon.CAR_YARD_FEATURE_SETTING, List.class);
        if (o == null) {
            List<FeatureSetting> settings = featureSettingConfig.getSettings();
            globalSettingService.set(YstCommon.CAR_YARD_FEATURE_SETTING, settings);
        }
    }

    @GetMapping("/setting/get")
    @ResponseBody
    public Response getFeatureSetting() {
        List<FeatureSetting> o = (List<FeatureSetting>) globalSettingService.get(YstCommon.CAR_YARD_FEATURE_SETTING, List.class);
        return Response.build(Status.NORMAL, null, o);
    }

}
