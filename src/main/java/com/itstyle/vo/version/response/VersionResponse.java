package com.itstyle.vo.version.response;

import lombok.Data;

@Data
public class VersionResponse {
    private String serviceCode;
    private String errorCode;
    private String errorDesc;
    private String downloadUrl;
    private String updateContent;
    private String versionCode;
}
