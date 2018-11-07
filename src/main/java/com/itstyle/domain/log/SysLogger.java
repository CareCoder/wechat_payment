package com.itstyle.domain.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_log")
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class SysLogger {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String action;
    private String desc;
    private Date logDate;

    public SysLogger(String username, String action, String desc, Date logDate) {
        this.username = username;
        this.action = action;
        this.desc = desc;
        this.logDate = logDate;
    }
}
