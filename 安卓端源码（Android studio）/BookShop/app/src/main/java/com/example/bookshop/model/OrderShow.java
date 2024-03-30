package com.example.bookshop.model;

import java.io.Serializable;

public class OrderShow implements Serializable {
    private String orderId;

    private Integer orderUserId;

    private String orderPhone;

    private String orderName;

    private String orderAddress;

    private String orderType;

    private String bookName;

    private String bookImage;

    private String bookDescription;

    private Double bookPrice;

    private Integer orderitemBookNumber;

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderUserId() {
        return orderUserId;
    }

    public void setOrderUserId(Integer orderUserId) {
        this.orderUserId = orderUserId;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public Integer getOrderitemBookNumber() {
        return orderitemBookNumber;
    }

    public void setOrderitemBookNumber(Integer orderitemBookNumber) {
        this.orderitemBookNumber = orderitemBookNumber;
    }

    public Double getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(Double bookPrice) {
        this.bookPrice = bookPrice;
    }

    @Override
    public String toString() {
        return "OrderShow{" +
                "orderId='" + orderId + '\'' +
                ", orderUserId=" + orderUserId +
                ", orderPhone='" + orderPhone + '\'' +
                ", orderName='" + orderName + '\'' +
                ", orderAddress='" + orderAddress + '\'' +
                ", orderType='" + orderType + '\'' +
                ", bookName='" + bookName + '\'' +
                ", bookDescription='" + bookDescription + '\'' +
                ", orderitemBookNumber=" + orderitemBookNumber +
                '}';
    }
}
