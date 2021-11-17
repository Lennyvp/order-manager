package com.lenart.ordermanager.service.order;

import com.lenart.ordermanager.service.order.pojo.Order;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    boolean checkOrderNumber(String orderNumber);

    String createOrder(Order order);

    double calcPriceWithDiscount(double price, int amount, String code);
}
