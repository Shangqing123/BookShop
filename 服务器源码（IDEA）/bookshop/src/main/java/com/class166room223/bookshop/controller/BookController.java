package com.class166room223.bookshop.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.class166room223.bookshop.dao.BookMapper;
import com.class166room223.bookshop.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * ClassName:BookController
 * Author:落魄山陈十一
 * Date:2019/6/9 20:09
 * Description:
 */
@RestController
public class BookController {
    @Autowired
    private BookMapper bookMapper;

    //管理员展示所有书籍
    @GetMapping("/listBookByManager")
    public Object listBook() {
        ArrayList<Book> books = bookMapper.listBook();
        return JSONObject.toJSON(books);
    }

    //用户分类展示所有书籍
    @GetMapping("/listBook")
    public String listBook(HttpServletRequest request) {
        String bookcategory = request.getParameter("bookcategory");
        ArrayList<Book> books  = new ArrayList<>();
        if(bookcategory.equals("综合")){
            books.addAll(bookMapper.listBook());
            System.out.println(books.toString());
        } else{
            System.out.println(bookcategory);
            books.addAll(bookMapper.listBookByCategory(bookcategory));
            System.out.println(books.toString());
        }
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(Book book : books) {
            System.out.println(book.toString());
            jsonArray.add(book);
        }
        jsonObject.put("books",jsonArray);
        return jsonObject.toJSONString();
    }

    @GetMapping("/searchBook")
    public String searchBook(HttpServletRequest request) {
        String booktxt = request.getParameter("booktxt");
        ArrayList<Book> books  = new ArrayList<>();
        books.addAll(bookMapper.searchBookByTxt(booktxt));
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(Book book : books) {
            System.out.println(book.toString());
            jsonArray.add(book);
        }
        jsonObject.put("books",jsonArray);
        return jsonObject.toJSONString();
    }

    //展示书籍详细信息
    @RequestMapping("/getBook")
    public String getBook(HttpServletRequest request) {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        Book book = bookMapper.selectByPrimaryKey(bookId);
        return JSONObject.toJSONString(book);
    }


    //管理员添加书籍
    @PostMapping("/addBook")
    public String addBook(HttpServletRequest request) {
        String obj = new String();
        try {
            obj = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Book book =  JSONObject.parseObject(obj, Book.class);
        book.setBookSales(0);
        bookMapper.insert(book);
        return "Add Success";
    }

    //添加书籍
    @PostMapping("/addBookImage")
    public String addBook(HttpServletRequest request, MultipartFile imgFile) {
        String bookName = request.getParameter("bookName");
        String bookImgPath = null;
        if(imgFile.isEmpty()) {
            bookImgPath = "http://47.100.121.231:8080//imgupload/default.jpg";
        } else {
            try {
                String fileName = imgFile.getOriginalFilename();
                fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + fileName;
                String path = "/root/imageupload/";
                File file = new File(path,fileName);
                //如果抽象路径名指定的文件不存在会自动创建文件
                imgFile.transferTo(file);
                bookImgPath = "http://47.100.121.231:8080//imgupload/" + fileName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        bookMapper.updateImgBybookName(bookName,bookImgPath);
        return "添加书本成功";
    }

    //删除书籍
    @GetMapping("/deleteBook")
    public String deleteBook(HttpServletRequest request) {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        bookMapper.deleteByPrimaryKey(bookId);
        return "Delete Success";
    }

    //修改书籍
    @PostMapping("/updateBook")
    public String  updateBook(HttpServletRequest request) {
        String obj = new String();
        try {
            obj = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Book book =  JSONObject.parseObject(obj, Book.class);
        bookMapper.updateByPrimaryKey(book);
        return "Update Success";
    }

    @PostMapping("/updateBookImg")
    public String updateBookImg(HttpServletRequest request, MultipartFile imgFile) {
        String bookName = request.getParameter("bookName");
        String bookImgPath = null;
        if(!imgFile.isEmpty()) {
            try {
                String fileName = imgFile.getOriginalFilename();
                fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + fileName;
                String path = "/root/imageupload/";
                File file = new File(path,fileName);
                //如果抽象路径名指定的文件不存在会自动创建文件
                imgFile.transferTo(file);
                bookImgPath = "http://47.100.121.231:8080//imgupload/" + fileName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        bookMapper.updateImgBybookName(bookName,bookImgPath);
        return null;
    }
}
