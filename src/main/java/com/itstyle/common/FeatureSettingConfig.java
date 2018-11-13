package com.itstyle.common;

import com.itstyle.domain.caryard.FeatureSetting;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties("feature")
public class FeatureSettingConfig {
    List<FeatureSetting> settings = new ArrayList<>();
}
