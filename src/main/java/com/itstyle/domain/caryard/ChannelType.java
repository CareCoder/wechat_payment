package com.itstyle.domain.caryard;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "channel_type")
@DynamicUpdate
public class ChannelType {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
