package com.itstyle.domain.exceptiongate;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "exception_gate")
public class ExceptionGate {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 操作时间
     */
    private Long operTime;

    /**
     * 操作通道
     */
    private String operChannel;

    /**
     * 操作类型
     */
    private String operType;

    /**
     * 操作时上传的图片的UUID
     */
    private String operImgUuid;
}
