package com.itstyle.domain.caryard;

import lombok.Data;

@Data
public class FeatureSetting {
    private String featureName;
    private String featureDefinition;
    private Boolean enable;
}
