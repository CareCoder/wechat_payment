package com.itstyle.domain.park.resp;

import com.google.gson.JsonElement;

public class Response {
    public int status;
    public String msg;
    public JsonElement data;

    public static Response build(int status, String msg, JsonElement data) {
        Response response = new Response();
        response.status = status;
        response.msg = msg;
        response.data = data;
        return response;
    }
}
