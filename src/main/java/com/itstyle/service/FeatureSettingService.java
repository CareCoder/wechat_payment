package com.itstyle.service;

import com.itstyle.domain.caryard.FeatureSetting;

import java.util.List;

public interface FeatureSettingService {
    List<FeatureSetting> list();

    void save(List<FeatureSetting> lists);

    void update(FeatureSetting featureSetting);

}
