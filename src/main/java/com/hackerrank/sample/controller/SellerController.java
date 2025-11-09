package com.hackerrank.sample.controller;

import com.hackerrank.sample.dto.SellerDetailDto;
import com.hackerrank.sample.service.ItemExtrasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    private final ItemExtrasService itemExtrasService;

    @Autowired
    public SellerController(ItemExtrasService itemExtrasService) {
        this.itemExtrasService = itemExtrasService;
    }

    @GetMapping("/{sellerId}")
    public SellerDetailDto getSellerDetail(@PathVariable String sellerId) {
        return itemExtrasService.getSellerDetail(sellerId);
    }
}


