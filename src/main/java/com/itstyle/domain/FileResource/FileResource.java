package com.itstyle.domain.FileResource;

import com.itstyle.utils.hibernate.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "file_resource",indexes = {@Index(name = "uuid_index", columnList = "uuid")})
public class FileResource extends BaseEntity {
    /**
     * 原文件名
     */
    private String originalName;
    /**
     * 新文件名
     */
    private String fileName;
    /**
     * 文件后缀
     */
    private String suffix;
    /**
     * UUID
     */
    private String uuid;
}
