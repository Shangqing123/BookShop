package com.class166room223.bookshop.dao;

import com.class166room223.bookshop.model.Cart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartMapper {
    int deleteByPrimaryKey(Integer cartId);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer cartId);

    Cart selectByUser(Integer userId);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
}