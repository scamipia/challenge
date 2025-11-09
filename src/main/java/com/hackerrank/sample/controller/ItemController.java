package com.hackerrank.sample.controller;

import com.hackerrank.sample.dto.ItemDetailDto;
import com.hackerrank.sample.dto.QuestionDto;
import com.hackerrank.sample.dto.RelatedItemDto;
import com.hackerrank.sample.dto.ReviewPageDto;
import com.hackerrank.sample.dto.ShippingOptionDto;
import com.hackerrank.sample.service.ItemExtrasService;
import com.hackerrank.sample.service.ItemService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemExtrasService itemExtrasService;

    @Autowired
    public ItemController(ItemService itemService, ItemExtrasService itemExtrasService) {
        this.itemService = itemService;
        this.itemExtrasService = itemExtrasService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDetailDto getItemDetail(@PathVariable String id) {
        return itemService.getItemDetail(id);
    }

    @GetMapping("/{id}/related")
    public List<RelatedItemDto> getRelatedItems(@PathVariable String id) {
        return itemExtrasService.getRelatedItems(id);
    }

    @GetMapping("/{id}/questions")
    public List<QuestionDto> getQuestions(@PathVariable String id) {
        return itemExtrasService.getQuestions(id);
    }

    @GetMapping("/{id}/reviews")
    public ReviewPageDto getReviews(
            @PathVariable String id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return itemExtrasService.getReviews(id, page, size);
    }

    @GetMapping("/{id}/shipping-options")
    public List<ShippingOptionDto> getShippingOptions(
            @PathVariable String id,
            @RequestParam(name = "zipcode", required = false) String zipcode
    ) {
        return itemExtrasService.getShippingOptions(id, zipcode);
    }
}

