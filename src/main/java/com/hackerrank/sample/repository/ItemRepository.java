package com.hackerrank.sample.repository;

import com.hackerrank.sample.model.Item;
import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Optional<Item> findById(String id);

    List<Item> findAll();
}

