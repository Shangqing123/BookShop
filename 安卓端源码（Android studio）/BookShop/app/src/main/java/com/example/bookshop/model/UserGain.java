package com.example.bookshop.model;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class UserGain extends LitePalSupport implements Serializable {
    private int GainId;
    private String address;
    private String phone;
    private String type = "非";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGainId() {
        return GainId;
    }

    public void setGainId(int gainId) {
        GainId = gainId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserGain{" +
                "GainId=" + GainId +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
