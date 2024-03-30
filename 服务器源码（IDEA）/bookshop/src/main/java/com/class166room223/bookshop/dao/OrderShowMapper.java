package com.class166room223.bookshop.dao;

import com.class166room223.bookshop.model.OrderShow;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderShowMapper {

    List<OrderShow> selectByPrimaryKey(Integer userId);

}