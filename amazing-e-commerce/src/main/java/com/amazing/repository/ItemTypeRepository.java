package com.amazing.repository;

import java.util.List;

import com.amazing.model.ItemType;

/**
 * ItemTypeRepository
 */
public interface ItemTypeRepository {

    void add(ItemType itemType);
    ItemType findById(Integer id);
    ItemType findByName(String name);
    List<ItemType> findAll();
    void update(ItemType itemType);
    void remove(ItemType itemType);
}