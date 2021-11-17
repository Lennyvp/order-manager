package com.lenart.ordermanager.service.order;

import com.lenart.ordermanager.service.discount.DiscountService;
import com.lenart.ordermanager.service.order.pojo.Order;
import com.lenart.ordermanager.service.product.ProductService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final JdbcTemplate jdbcTemplate;

    private final DiscountService discountService;
    private final ProductService productService;

    public OrderServiceImpl(JdbcTemplate jdbcTemplate, DiscountService discountService, ProductService productService) {
        this.jdbcTemplate = jdbcTemplate;
        this.discountService = discountService;
        this.productService = productService;
    }

    @Override
    public boolean checkOrderNumber(String orderNumber) {
        String sql = "select order_number from manager.orders where order_number = ?";
        return jdbcTemplate.queryForObject(sql, String.class) == null;
    }

    @Transactional
    @Override
    public String createOrder(Order order) {
        String code = order.getDiscountCode();
        int productId = order.getProductId();
        int amount = setPositiveAmount(order.getAmount());
        double productPrice;
        if(productService.findById(productId).isPresent()) {
            productPrice = productService.findById(productId).get().getPrice();
        } else {
            return null;
        }
        double orderPrice;

        String orderNumber = UUID.randomUUID().toString();
//        String orderNumber = generateUUID();

        if(!discountService.isCodeUsed(code)){
            int value = discountService.discountValue(code);
            discountService.save(code, value);
            orderPrice = calcPriceWithDiscount(productPrice, amount, value);
            String sql = "insert into manager.orders (order_number, discount_code, product_id, amount, price) values (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, orderNumber, code, productId, amount, orderPrice);
        } else {
            orderPrice = calcPriceWithDiscount(productPrice, amount, 0);
            String sql = "insert into manager.orders (order_number, product_id, amount, price) values (?, ?, ?, ?)";
            jdbcTemplate.update(sql, orderNumber, productId, amount, orderPrice);
        }

        return orderNumber;
    }


    private double calcPriceWithDiscount(double price, int amount ,int value) {
        double totalPrice = price * amount * (1 - (double) value / 100);
        return (double) Math.round(totalPrice * 100) / 100;
    }

    @Override
    public double calcPriceWithDiscount(double price, int amount , String code) {
        int value = 0;
        if(!discountService.isCodeUsed(code)) {
            value = discountService.discountValue(code);
        }
        double totalPrice = price * amount * (1 - (double) value / 100);
        return (double) Math.round(totalPrice * 100) / 100;
    }

    private String generateUUID() {
        String orderNumber;
        do {
            orderNumber = UUID.randomUUID().toString();
        }
        while(!checkOrderNumber(orderNumber));
        return orderNumber;
    }

    private int setPositiveAmount(int amount) {
        if(amount < 1) {
            amount = 1;
        }
        return amount;
    }


}
