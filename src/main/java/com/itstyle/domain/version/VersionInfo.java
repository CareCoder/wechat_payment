package com.itstyle.domain.version;

import lombok.Data;

@Data
public class VersionInfo {
    private String oldVersionCode;
    private String newVersionCode;
    private String updateContent;
}
