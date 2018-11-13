package com.itstyle.domain.caryard;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "feature_setting")
@DynamicUpdate
public class FeatureSetting {

    @Id
    @GeneratedValue
    private Long id;
    private String featureName;
    private String featureDefinition;
    private Boolean enable;
}
