package com.itstyle.domain.msg.req;

public class TextReqMessage extends BaseRequestMessage
{
    // 消息内容
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