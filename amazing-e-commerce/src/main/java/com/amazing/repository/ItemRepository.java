package com.amazing.repository;

import java.util.List;
import java.util.Map;

import com.amazing.model.Item;

/**
 * ItemRepository
 */
public interface ItemRepository {

    void add(Item item);
    Item findById(String id);
    List<Item> findByTypeId(Integer typeId);
    List<Item> findAll();
    List<Map<String,Object>> findAll2();
    void remove(Item item);
    void update(Item item);
    

}