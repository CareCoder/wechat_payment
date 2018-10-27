package com.itstyle.domain.msg.req;

public class ImageMessage extends BaseRequestMessage
{
    // 图片链接
    private String PicUrl;

    public String getPicUrl ()
    {
        return PicUrl;
    }

    public void setPicUrl (String picUrl)
    {
        PicUrl = picUrl;
    }
}
