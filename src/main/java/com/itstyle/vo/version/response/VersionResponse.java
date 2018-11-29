package com.itstyle.vo.version.response;

import lombok.Data;

@Data
public class VersionResponse {
    private String serviceCode;
    private int errorCode;
    private String errorDesc;
    private String downloadUrl;
    private String updateContent;
    private Integer versionCode;
}
