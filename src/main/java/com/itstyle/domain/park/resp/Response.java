package com.itstyle.domain.park.resp;

public class Response {
    public int status;
    public String msg;
    public Object data;

    public static Response build(int status, String msg, Object data) {
        Response response = new Response();
        response.status = status;
        response.msg = msg;
        response.data = data;
        return response;
    }
}
