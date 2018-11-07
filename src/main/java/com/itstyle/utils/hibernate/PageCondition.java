package com.itstyle.utils.hibernate;


public class PageCondition {
    private int pageNum = 0;

    private int pageSize = 15;

    private String order;

    private String orderBy = "DESC";

    public int getPageNum() {
        return pageNum < 0 ? 0 : pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize == 0 ? 15 : pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
