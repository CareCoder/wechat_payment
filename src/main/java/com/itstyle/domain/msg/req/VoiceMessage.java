package com.itstyle.domain.msg.req;

public class VoiceMessage extends BaseRequestMessage
{
    // 媒体ID
    private String MediaId;
    // 语音格式
    private String Format;

    public String getMediaId ()
    {
        return MediaId;
    }

    public void setMediaId (String mediaId)
    {
        MediaId = mediaId;
    }

    public String getFormat ()
    {
        return Format;
    }

    public void setFormat (String format)
    {
        Format = format;
    }
}