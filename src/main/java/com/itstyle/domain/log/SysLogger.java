package com.itstyle.domain.log;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "sys_log")
@EntityListeners(AuditingEntityListener.class)
public class SysLogger implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String action;
    private String tDescribe;
    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @CreatedDate
    private Date logDate;

    public SysLogger() {}

    public SysLogger(String username, String action, String tDescribe, Date logDate) {
        this.username = username;
        this.action = action;
        this.tDescribe = tDescribe;
        this.logDate = logDate;
    }
}
