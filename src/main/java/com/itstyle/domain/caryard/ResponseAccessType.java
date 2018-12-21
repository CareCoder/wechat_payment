package com.itstyle.domain.caryard;

import lombok.Data;

@Data
public class ResponseAccessType {
    private Long id;
    private String channelName;
    private Long channelTypeId;
    private String ip;
    private String camera;
    private String camera2;//辅助相机ip地址
    private String channelTypeName;
}
