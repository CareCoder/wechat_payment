package com.itstyle.control;

import com.itstyle.common.FeatureSettingConfig;
import com.itstyle.common.PageResponse;
import com.itstyle.common.SystemLoggerHelper;
import com.itstyle.domain.caryard.FeatureSetting;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;
import com.itstyle.service.FeatureSettingService;
import com.itstyle.utils.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/feature")
public class FeatureSettingController {

    private FeatureSettingService featureSettingService;
    private FeatureSettingConfig featureSettingConfig;

    @Autowired
    public FeatureSettingController(FeatureSettingService featureSettingService,
                                    FeatureSettingConfig featureSettingConfig) {
        this.featureSettingService = featureSettingService;
        this.featureSettingConfig = featureSettingConfig;
    }

    @GetMapping("/setting/get")
    @ResponseBody
    public PageResponse<FeatureSetting> getFeatureSetting() {
        List<FeatureSetting> list = featureSettingService.list();
        if (list == null || list.size() == 0) {
            List<FeatureSetting> settings = featureSettingConfig.getSettings();
            featureSettingService.save(settings);
            return new PageResponse<>(0L, settings);
        } else {
            return new PageResponse<>(0L, list);
        }
    }

    @PostMapping("/setting/edit")
    @ResponseBody
    public Response edit(FeatureSetting featureSetting) {
        AssertUtil.assertNotNull(featureSetting, () -> new BusinessException("feature setting is null"));
        AssertUtil.assertNotNull(featureSetting.getId(), () -> new BusinessException("功能id不能为空"));
        AssertUtil.assertNotEmpty(featureSetting.getFeatureName(), () -> new BusinessException("功能名称不能为空"));
        AssertUtil.assertNotEmpty(featureSetting.getFeatureDefinition(), () -> new BusinessException("功能定义不能为空"));
        featureSettingService.update(featureSetting);
        SystemLoggerHelper.log("设置", "功能性设置");
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/setting/page")
    public String getFeatureSettingPage() {
        return "/backend/feature-setting";
    }

}
