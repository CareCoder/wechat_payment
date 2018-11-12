package com.itstyle.domain.FileResource;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileResourceBo extends FileResource {
    private String url;
}
