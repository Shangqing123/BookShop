package com.class166room223.bookshop.controller;


import com.alibaba.fastjson.JSONObject;
import com.class166room223.bookshop.dao.CartMapper;
import com.class166room223.bookshop.dao.UserMapper;
import com.class166room223.bookshop.model.Cart;
import com.class166room223.bookshop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * ClassName:UserController
 * Author:落魄山陈十一
 * Date:2019/6/9 13:58
 * Description:
 */
@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CartMapper cartMapper;

    //根据id获得用户信息
    @GetMapping("/getuser")
    public String getuser(HttpServletRequest request) throws JSONException {
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        User user = userMapper.selectByUserName(userName);
        if(!user.getUserPassword().equals(userPassword)) {
            user=null;
        }
        return JSONObject.toJSONString(user);

    }

    //判断是否可以登录
    @GetMapping("/logincheck")
    public String logincheck(HttpServletRequest request) {
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        User user = userMapper.selectByUserName(userName);
        if(user==null) {
            return "用户名不存在";
        } else if(!user.getUserPassword().equals(userPassword)) {
            return "密码错误";
        } else if(user.getUserType().equals("manager")){
            return "管理员登录成功";
        } else {
            return "用户登陆成功";
        }
    }

    //进行用户注册
    @PostMapping("/regiseter")
    public String register(HttpServletRequest request) throws JSONException {
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        User user = userMapper.selectByUserName(userName);
        JSONObject jsonuser = new JSONObject();
        if(user!=null) {
            jsonuser.put("message","该用户名已注册过");
            return jsonuser.toString();
        }
        user = new User();
        System.out.println(userName + userPassword);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setUserCreatetime(new Date());
        user.setUserType("user");
        userMapper.insert(user);
        Cart cart = new Cart();
        cart.setCartUserId(userMapper.selectByUserName(userName).getUserId());
        cartMapper.insert(cart);
        jsonuser.put("message","注册成功");
        return jsonuser.toString();
    }
}
