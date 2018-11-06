package com.itstyle.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DataPage<T> {

    private List<T> list;

    private int page;

    private int size;

    private long total;

    private long pages;

    private int startRow;

    public DataPage() {}

    public DataPage(int page, int size, long total) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.pages = (total + size -1 ) / size;
        this.startRow = (page -1) * size;
        this.list = new ArrayList<>();
    }
}
