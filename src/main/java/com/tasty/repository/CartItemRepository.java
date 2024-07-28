package com.tasty.repository;

import com.tasty.model.Cart;
import com.tasty.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


}
