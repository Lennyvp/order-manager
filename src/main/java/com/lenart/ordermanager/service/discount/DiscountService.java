package com.lenart.ordermanager.service.discount;

import com.lenart.ordermanager.service.discount.pojo.Discount;

import java.util.Optional;

public interface  DiscountService {
    Optional<Discount> findByCode(String code);

    void save(String code, int value);

    int discountValue(String code);

    boolean isCodeUsed(String code);

    boolean isValidCode(String code);

    Optional<String> findCodeByCode(String code);
}
