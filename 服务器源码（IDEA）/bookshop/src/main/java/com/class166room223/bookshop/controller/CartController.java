package com.class166room223.bookshop.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.class166room223.bookshop.dao.BookMapper;
import com.class166room223.bookshop.dao.CartItemMapper;
import com.class166room223.bookshop.dao.CartMapper;
import com.class166room223.bookshop.model.Book;
import com.class166room223.bookshop.model.Cart;
import com.class166room223.bookshop.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * ClassName:CartController
 * Author:落魄山陈十一
 * Date:2019/6/9 20:20
 * Description:
 */
@RestController
public class CartController {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private CartItemMapper cartItemMapper;
    @Autowired
    private BookMapper bookMapper;

    //点击加入购物车
    @PostMapping("/addBookToCart")
    public String addBookToCart(HttpServletRequest request) {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int userId = Integer.parseInt(request.getParameter("userId"));
        Cart cart = cartMapper.selectByUser(userId);
        CartItem cartItem = cartItemMapper.selectByCartBook(cart.getCartId(),bookId);
        if(cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCartitemCartId(cart.getCartId());
            cartItem.setCartitemBookId(bookId);
            cartItem.setCartitemBookNumber(1);
            cartItemMapper.insert(cartItem);
        } else {
            cartItemMapper.ascByBookIdCartIdone(cart.getCartId(),bookId);
        }
        return "加入成功";
    }

    //展示购物车
    @RequestMapping("/getCart")
    public String getCart(HttpServletRequest request) {

        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArray1 = new JSONArray();
        int userId = Integer.parseInt(request.getParameter("userId"));
        Cart cart = cartMapper.selectByUser(userId);
        ArrayList<CartItem> items = cartItemMapper.listAll(cart.getCartId());
        ArrayList<Book> books = new ArrayList<>();
        for(CartItem cartItem : items) {
            jsonArray.add(cartItem);
            jsonArray1.add(bookMapper.selectByPrimaryKey(cartItem.getCartitemBookId()));
        }
        object.put("cartitems",jsonArray);
        object.put("books",jsonArray1);
        return object.toJSONString();
    }

    //从购物车删除
    @RequestMapping("/deleteCartItem")
    public String deleteCartItem(HttpServletRequest request) {
        int cartitemId = Integer.parseInt(request.getParameter("cartitemId"));
        cartItemMapper.deleteByPrimaryKey(cartitemId);
        return null;
    }

    @RequestMapping("/addCartItem")
    public String addCartItem(HttpServletRequest request) {
        int cartitemId = Integer.parseInt(request.getParameter("cartitemId"));
        cartItemMapper.ascByBookIdCartId(cartitemId);
        return "增加成功";
    }

    @RequestMapping("/descCartItem")
    public String descCartItem(HttpServletRequest request) {
        int cartitemId = Integer.parseInt(request.getParameter("cartitemId"));
        cartItemMapper.descByBookIdCartId(cartitemId);
        return "减少成功";
    }

    //清空购物车
    @GetMapping("/deleteCart")
    public String deleteCart(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getParameter("userId"));
        Cart cart = cartMapper.selectByPrimaryKey(userId);
        cartItemMapper.deleteByPrimaryKey(cart.getCartId());
        return null;
    }
}
