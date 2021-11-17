package com.lenart.ordermanager.service.product;

import com.lenart.ordermanager.service.product.pojo.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();

    Optional<Product> findById(int id);

}
