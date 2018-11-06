package com.itstyle.common;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    private int code;
    private String msg;
    private Long count;
    private List<T> data;

    public PageResponse(Long count, List<T> data) {
        this.count = count;
        this.data = data;
    }

    public PageResponse(int code, String msg, Long count, List<T> data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }
}
