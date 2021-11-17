package com.lenart.ordermanager.controller;

import com.lenart.ordermanager.service.discount.DiscountService;
import org.springframework.web.bind.annotation.*;

@RestController
public class DiscountController {

    private DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @CrossOrigin
    @GetMapping("discounts")
    public @ResponseBody Integer getDiscountValue(@RequestParam("code") String code ) {
        if(discountService.isCodeUsed(code)) {
            return null;
        }
        return discountService.discountValue(code);
    }
}
