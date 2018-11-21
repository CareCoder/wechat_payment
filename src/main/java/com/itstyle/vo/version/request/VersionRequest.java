package com.itstyle.vo.version.request;

import lombok.Data;

@Data
public class VersionRequest {
    private String serviceCode;
    private String dataSrc;
    private String keyWord;
    private String versionCode;
}
