package com.itstyle.domain.account;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "role")
@DynamicUpdate
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Role implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
