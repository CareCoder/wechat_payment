package com.itstyle.common;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;

import java.util.List;
import java.util.function.Supplier;

public class Pagination<T> {

    private static final int MAX_SIZE = 50;

    public PageResponse<T> execute(int page, int size, Supplier<List<T>> supplier) {
        AssertUtil.assertTrue(size <= MAX_SIZE, () -> new BusinessException("size should le " + MAX_SIZE));
        PageHelper.startPage(page, size);
        List<T> data = supplier.get();
        PageInfo<T> wrapper = new PageInfo<>(data);
        PageResponse<T> response = new PageResponse<>(wrapper.getTotal(), wrapper.getList());
        return response;
    }

    public PageResponse<T> execute(int page, int size, int code, String msg, Supplier<List<T>> supplier) {
        AssertUtil.assertTrue(size <= MAX_SIZE, () -> new BusinessException("size should le " + MAX_SIZE));
        PageHelper.startPage(page, size);
        List<T> data = supplier.get();
        PageInfo<T> wrapper = new PageInfo<>(data);
        PageResponse<T> response = new PageResponse<>(code, msg, wrapper.getTotal(), wrapper.getList());
        return response;
    }
}
