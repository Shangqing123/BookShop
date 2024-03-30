package com.class166room223.bookshop.dao;

import com.class166room223.bookshop.model.OrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer orderitemId);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer orderitemId);

    ArrayList<OrderItem> listAll(String orderId);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
}