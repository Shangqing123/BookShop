package com.example.bookshop.model;

import java.io.Serializable;

/**
 * 购物车
 */
public class CartItem implements Serializable {
    private Integer cartitemId;

    private Integer cartitemCartId;

    private Integer cartitemBookId;

    private Integer cartitemBookNumber;

    public Integer getCartitemId() {
        return cartitemId;
    }

    public void setCartitemId(Integer cartitemId) {
        this.cartitemId = cartitemId;
    }

    public Integer getCartitemCartId() {
        return cartitemCartId;
    }

    public void setCartitemCartId(Integer cartitemCartId) {
        this.cartitemCartId = cartitemCartId;
    }

    public Integer getCartitemBookId() {
        return cartitemBookId;
    }

    public void setCartitemBookId(Integer cartitemBookId) {
        this.cartitemBookId = cartitemBookId;
    }

    public Integer getCartitemBookNumber() {
        return cartitemBookNumber;
    }

    public void setCartitemBookNumber(Integer cartitemBookNumber) {
        this.cartitemBookNumber = cartitemBookNumber;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartitemId=" + cartitemId +
                ", cartitemCartId=" + cartitemCartId +
                ", cartitemBookId=" + cartitemBookId +
                ", cartitemBookNumber=" + cartitemBookNumber +
                '}';
    }
}