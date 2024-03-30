package com.class166room223.bookshop.model;

public class CartItem {
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
}