package com.tasty.service;

import com.tasty.model.*;
import com.tasty.repository.AddressRepository;
import com.tasty.repository.OrderItemRepository;
import com.tasty.repository.OrderRepository;
import com.tasty.repository.UserRepository;
import com.tasty.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CartService cartService;

    @Override
    public Order createOrder(OrderRequest order, User user) throws Exception {
        Address shippingAddress = order.getDeliveryAddress();

        Address saveAddress =addressRepository.save(shippingAddress);
        if(!user.getAddresses().contains(saveAddress)){
            user.getAddresses().add((saveAddress));
            userRepository.save(user);
        }

        Restaurant restaurant = restaurantService.findRestaurantById(order.getRestaurantId());
        Order createOrder =  new Order();
        createOrder.setCustomer(user);
        createOrder.setCreateAt(new Date());
        createOrder.setOrderStatus("PENDING");
        createOrder.setDeliveryAddress(saveAddress);
        createOrder.setRestaurant(restaurant);

        Cart cart = cartService.findCartByUserId(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem: cart.getItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setFood(cartItem.getFood());
            orderItem.setIngredients(cartItem.getIngredients());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getTotalPrice());

            OrderItem savedOrderItem= orderItemRepository.save(orderItem);
            orderItems.add(savedOrderItem);
        }
        Long totalPrice = cartService.calculateCartTotals(cart);
        createOrder.setItems(orderItems);
        createOrder.setTotalPrice(cart.getTotal());

        Order savedOrder =orderRepository.save(createOrder);
        restaurant.getOrders().add(savedOrder);


        return createOrder;
    }

    @Override
    public Order updateOrder(Long orderId, String orderStatus) throws Exception {

        Order order=findOrderById(orderId);
        if(orderStatus.equals("OUT_FOR_DELIVERY")
                || orderStatus.equals("DELIVERD")
                || orderStatus.equals("COMPLETED")
                || orderStatus.equals("PENDING")){
            order.setOrderStatus(orderStatus);
            return orderRepository.save(order);
        }
        throw new Exception("please select a valid order status");
    }

    @Override
    public void cancelOder(long orderId) throws Exception {

        Order order =findOrderById(orderId);
        orderRepository.deleteById(orderId);

    }

    @Override
    public List<Order> getUserOrder(Long userId) throws Exception {

        return orderRepository.findByCustomerId(userId);
    }

    @Override
    public List<Order> getRestaurantsOrder(Long restaurantId, String orderStatus) throws Exception {
        List<Order> orders= orderRepository.findByRestaurantId(restaurantId);
        if(orderStatus!=null){
            orders=orders.stream().filter(order ->
                    order.getOrderStatus().equals(orderStatus)).collect(Collectors.toList());
        }
        return orders;
    }

    @Override
    public Order findOrderById(Long orderId) throws Exception {
        Optional<Order> optionalOrder=orderRepository.findById(orderId);
        if(optionalOrder.isEmpty()){
            throw new Exception("order not found");
        }
        return optionalOrder.get();
    }
}
