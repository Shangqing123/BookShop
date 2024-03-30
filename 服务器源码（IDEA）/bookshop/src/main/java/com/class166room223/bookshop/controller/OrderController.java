package com.class166room223.bookshop.controller;

import com.alibaba.fastjson.JSONObject;
import com.class166room223.bookshop.dao.*;
import com.class166room223.bookshop.model.CartItem;
import com.class166room223.bookshop.model.Order;
import com.class166room223.bookshop.model.OrderItem;
import com.class166room223.bookshop.model.OrderShow;
import com.class166room223.bookshop.utils.GenerateSelfId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:OrderController
 * Author:落魄山陈十一
 * Date:2019/6/9 20:48
 * Description:
 */
@RestController
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderShowMapper orderShowMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private CartItemMapper cartItemMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private UserMapper userMapper;
    //添加订单
    @RequestMapping("/addOrder")
    public String addOrder(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String orderPhone = request.getParameter("phone");
        String orderName = userMapper.selectByPrimaryKey(userId).getUserName();
        String orderAddress = request.getParameter("address");
        int cartId = cartMapper.selectByUser(userId).getCartId();

        Order order = new Order();
        String orderId = GenerateSelfId.getSelfIdByUUId();
        order.setOrderId(orderId);
        order.setOrderUserId(userId);
        order.setOrderPhone(orderPhone);
        order.setOrderAddress(orderAddress);
        order.setOrderName(orderName);
        order.setOrderType("noreceive");
        orderMapper.insert(order);

        ArrayList<CartItem> cartItems = cartItemMapper.listAll(cartId);
        for(CartItem cartItem : cartItems) {
            bookMapper.updateSalesByBookId(cartItem.getCartitemBookId());


                OrderItem orderItem = new OrderItem();
                orderItem.setOrderitemOrderId(orderId);
                orderItem.setOrderitemBookId(cartItem.getCartitemBookId());
                orderItem.setOrderitemBookNumber(cartItem.getCartitemBookNumber());
                orderItemMapper.insert(orderItem);
        }
        cartItemMapper.deleteByCartId(cartId);

        return null;
    }

    //查看订单
    @GetMapping("/getOrder")
    public Object getOrder(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getParameter("userId"));
        ArrayList<Order> orders = orderMapper.listAll(userId);
        return JSONObject.toJSON(orders);
    }

    //查看订单详情
    @GetMapping("/getOrderItem")
    public Object getOrderitem(HttpServletRequest request) {
        String orderId = request.getParameter("orderId");
        ArrayList<OrderItem> orderItems = orderItemMapper.listAll(orderId);
        return JSONObject.toJSON(orderItems);
    }

    //确认收获
    @GetMapping("/updateOrder")
    public String updateOrder(HttpServletRequest request) {
        String orderId = request.getParameter("orderId");
        orderMapper.updateTypeByPrimaryKey(orderId);
        return "receive success";
    }

    //查看订单
    @GetMapping("/getOrderShow")
    public Object getOrderShow(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getParameter("userId"));
        List<OrderShow> orders = orderShowMapper.selectByPrimaryKey(userId);
        return JSONObject.toJSON(orders);
    }
}
