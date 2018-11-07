package com.itstyle.common;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Data
public class PageResponse<T> {
    private int code;
    private String msg;
    private Long count;
    private List<T> data;

    public PageResponse(Long count, List<T> data) {
        code = 0;
        msg = "";
        this.count = count;
        this.data = data;
    }

    public PageResponse(int code, String msg, Long count, List<T> data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    public static PageResponse build(Page page) {
        return new PageResponse<>(page.getTotalElements(), page.getContent());
    }

    public static PageRequest getPageRequest(int page, int limit) {
        return new PageRequest(page - 1, limit);
    }
}
