package com.tasty.request;

import com.tasty.model.Address;
import lombok.Data;

@Data
public class OrderRequest {

    private Long restaurantId;

    private Address deliveryAddress;

}
