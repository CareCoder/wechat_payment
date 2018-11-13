package com.itstyle.service.impl;

import com.itstyle.domain.caryard.FeatureSetting;
import com.itstyle.mapper.FeatureSettingMapper;
import com.itstyle.service.FeatureSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeatureSettingServiceImpl implements FeatureSettingService {

    private FeatureSettingMapper featureSettingMapper;

    @Autowired
    public FeatureSettingServiceImpl(FeatureSettingMapper featureSettingMapper) {
        this.featureSettingMapper = featureSettingMapper;
    }

    @Override
    public List<FeatureSetting> list() {
        return featureSettingMapper.findAll();
    }

    @Override
    public void save(List<FeatureSetting> lists) {
        featureSettingMapper.save(lists);
    }

    @Override
    public void update(FeatureSetting featureSetting) {
        featureSettingMapper.save(featureSetting);
    }
}
