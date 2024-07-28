package com.tasty.controller;

import com.tasty.model.CartItem;
import com.tasty.model.Order;
import com.tasty.model.User;
import com.tasty.request.AddCartItemRequest;
import com.tasty.request.OrderRequest;
import com.tasty.request.UpdateCartItemRequest;
import com.tasty.service.CartService;
import com.tasty.service.OrderService;
import com.tasty.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;


    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest req,
                                                  @RequestHeader("Authorization") String jwt)throws Exception{
        User user= userService.findUserByJwtToken(jwt);
        Order order = orderService.createOrder(req,user);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/order/user")
    public ResponseEntity<List<Order>> getOrderHistory(@RequestBody OrderRequest req,
                                             @RequestHeader("Authorization") String jwt)throws Exception{
        User user= userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.getUserOrder(user.getId());

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }


}
