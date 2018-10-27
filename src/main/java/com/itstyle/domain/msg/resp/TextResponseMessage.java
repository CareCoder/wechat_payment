package com.itstyle.domain.msg.resp;

public class TextResponseMessage extends BaseResponseMessage
{
    // 回复的消息内容
    private String Content;

    public String getContent ()
    {
        return Content;
    }

    public void setContent (String content)
    {
        Content = content;
    }
}