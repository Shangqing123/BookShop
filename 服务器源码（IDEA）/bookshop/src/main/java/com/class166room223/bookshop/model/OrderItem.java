package com.class166room223.bookshop.model;

public class OrderItem {
    private Integer orderitemId;

    private String orderitemOrderId;

    private Integer orderitemBookId;

    private Integer orderitemBookNumber;

    public Integer getOrderitemId() {
        return orderitemId;
    }

    public void setOrderitemId(Integer orderitemId) {
        this.orderitemId = orderitemId;
    }

    public String getOrderitemOrderId() {
        return orderitemOrderId;
    }

    public void setOrderitemOrderId(String orderitemOrderId) {
        this.orderitemOrderId = orderitemOrderId == null ? null : orderitemOrderId.trim();
    }

    public Integer getOrderitemBookId() {
        return orderitemBookId;
    }

    public void setOrderitemBookId(Integer orderitemBookId) {
        this.orderitemBookId = orderitemBookId;
    }

    public Integer getOrderitemBookNumber() {
        return orderitemBookNumber;
    }

    public void setOrderitemBookNumber(Integer orderitemBookNumber) {
        this.orderitemBookNumber = orderitemBookNumber;
    }
}