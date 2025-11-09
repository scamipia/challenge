package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.ItemDetailDto;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Item;
import com.hackerrank.sample.repository.ItemRepository;
import com.hackerrank.sample.service.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDetailDto getItemDetail(String id) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new NoSuchResourceFoundException("No se encontró el item con id " + id));

        return itemMapper.toDto(item);
    }
}

