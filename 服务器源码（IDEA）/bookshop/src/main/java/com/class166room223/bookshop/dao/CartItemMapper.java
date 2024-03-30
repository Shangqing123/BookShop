package com.class166room223.bookshop.dao;

import com.class166room223.bookshop.model.CartItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface CartItemMapper {
    int deleteByPrimaryKey(Integer cartitemId);

    int deleteByCartId(Integer cartId);

    int insert(CartItem record);

    int insertSelective(CartItem record);

    CartItem selectByPrimaryKey(Integer cartitemId);

    CartItem selectByCartBook(Integer cartId, Integer bookId);

    ArrayList<CartItem> listAll(Integer cartId);

    int updateByPrimaryKeySelective(CartItem record);

    int updateByPrimaryKey(CartItem record);

    int deleteByBookIdCartId(Integer cartId,Integer bookId);

    int descByBookIdCartId(Integer cartitemId);

    int ascByBookIdCartId(Integer cartitemId);

    int ascByBookIdCartIdone(Integer cartId,Integer bookId);
}