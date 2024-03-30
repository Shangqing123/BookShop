package com.class166room223.bookshop.dao;

import com.class166room223.bookshop.model.Book;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface BookMapper {
    int deleteByPrimaryKey(Integer bookId);

    int insert(Book record);

    int insertSelective(Book record);

    Book selectByPrimaryKey(Integer bookId);

    Book selectByUserName(String bookName);

    ArrayList<Book> listBook();

    ArrayList<Book> listBookByCategory(String bookCategory);

    ArrayList<Book> searchBookByTxt(String booktxt);

    int updateByPrimaryKeySelective(Book record);

    int updateByPrimaryKey(Book record);

    int updateImgBybookName(String bookName,String bookImage);

    int updateSalesByBookId(Integer bookId);
}