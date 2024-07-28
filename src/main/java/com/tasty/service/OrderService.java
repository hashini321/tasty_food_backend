package com.tasty.service;

import com.tasty.model.Order;
import com.tasty.model.User;
import com.tasty.request.OrderRequest;


import java.util.List;

public interface OrderService {

    public Order createOrder(OrderRequest order, User user) throws Exception;

    public Order updateOrder(Long orderId, String orderStatus)throws Exception;

    public void cancelOder(long orderId) throws Exception;

    public List<Order> getUserOrder(Long userId)throws Exception;

    public List<Order> getRestaurantsOrder(Long restaurantId, String orderStatus) throws Exception;

    public Order findOrderById(Long orderId) throws Exception;
}
