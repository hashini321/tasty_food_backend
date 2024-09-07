package com.tasty.controller;

import com.tasty.model.Food;
import com.tasty.model.User;
import com.tasty.service.FoodService;
import com.tasty.service.RestaurantService;
import com.tasty.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {
    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFood(@RequestParam String name,
                                           @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Food> foods=foodService.searchFood(name);
        return new ResponseEntity<>(foods, HttpStatus.CREATED);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Food>> getRestaurantsFood(@RequestParam boolean vegetarian,
                                                 @RequestParam(required = false) boolean seasonal,
                                                 @RequestParam(required = false) boolean nonveg,
                                                 @RequestParam (required = false) String food_category,
                                                 @PathVariable Long restaurantId,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Food> foods=foodService.getRestaurantsFood(restaurantId,vegetarian,nonveg,seasonal,food_category);
        return new ResponseEntity<>(foods, HttpStatus.CREATED);
    }


}
