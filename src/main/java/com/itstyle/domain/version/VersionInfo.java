package com.itstyle.domain.version;

import lombok.Data;

@Data
public class VersionInfo {
    private Integer versionCode;
    private String updateContent;
    private String filename;
    private String uuid;
}
