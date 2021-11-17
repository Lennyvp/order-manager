package com.lenart.ordermanager.controller;

import com.lenart.ordermanager.service.order.OrderService;
import com.lenart.ordermanager.service.order.pojo.Order;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @CrossOrigin
    @PostMapping("orders")
    public String createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @CrossOrigin
    @GetMapping("orders")
    public double getTotalPrice(@RequestParam("price") double price,
                                @RequestParam("amount") int amount,
                                @RequestParam("code") String code) {
        return orderService.calcPriceWithDiscount(price, amount, code);
    }

}
