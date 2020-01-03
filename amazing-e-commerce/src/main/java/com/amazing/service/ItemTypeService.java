package com.amazing.service;

import java.util.List;

import com.amazing.model.ItemType;
import com.amazing.repository.ItemTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ItemTypeService
 */
@Service
public class ItemTypeService {

    private ItemTypeRepository repository;

    @Autowired
    public ItemTypeService(ItemTypeRepository repository){
        this.repository = repository;
    }

    public List<ItemType> getAll(){
        return repository.findAll();
    }

    public ItemType getItemType(Integer id){
        return repository.findById(id);
    }

    public ItemType getItemType(String name){
        return repository.findByName(name);
    }
}