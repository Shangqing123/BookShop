package com.example.bookshop.model;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    private String TvName;
    private Double TvPrice;
    private String TvAddress;
    private String TvPhone;
    private String TvDetail;
    private Integer TvCount;
    private Double TvOrderAll;
    private Boolean state;

    public String getTvName() {
        return TvName;
    }

    public void setTvName(String tvName) {
        TvName = tvName;
    }

    public Double getTvPrice() {
        return TvPrice;
    }

    public void setTvPrice(Double tvPrice) {
        TvPrice = tvPrice;
    }

    public String getTvAddress() {
        return TvAddress;
    }

    public void setTvAddress(String tvAddress) {
        TvAddress = tvAddress;
    }

    public String getTvPhone() {
        return TvPhone;
    }

    public void setTvPhone(String tvPhone) {
        TvPhone = tvPhone;
    }

    public String getTvDetail() {
        return TvDetail;
    }

    public void setTvDetail(String tvDetail) {
        TvDetail = tvDetail;
    }

    public Integer getTvCount() {
        return TvCount;
    }

    public void setTvCount(Integer tvCount) {
        TvCount = tvCount;
    }

    public Double getTvOrderAll() {
        return TvOrderAll;
    }

    public void setTvOrderAll(Double tvOrderAll) {
        TvOrderAll = tvOrderAll;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "TvName='" + TvName + '\'' +
                ", TvPrice=" + TvPrice +
                ", TvAddress='" + TvAddress + '\'' +
                ", TvPhone='" + TvPhone + '\'' +
                ", TvDetail='" + TvDetail + '\'' +
                ", TvCount=" + TvCount +
                ", TvOrderAll=" + TvOrderAll +
                ", state='" + state + '\'' +
                '}';
    }
}
