package com.itstyle.domain.caryard;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "access_type")
@DynamicUpdate
public class AccessType {

    @Id
    @GeneratedValue
    private Long id;
    private String channelName;
    private Long channelTypeId;
    private String ip;
    private String camera;
}
