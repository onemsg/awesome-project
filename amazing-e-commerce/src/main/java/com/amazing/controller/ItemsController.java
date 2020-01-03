package com.amazing.controller;

import java.util.List;
import java.util.Map;

import com.amazing.model.Item;
import com.amazing.service.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * ItemsController
 */
@RestController
@RequestMapping(value = "/api/items")
public class ItemsController {

    @Autowired
    private ItemService service;

    @GetMapping(value="/{itemId}")
    public Item getItem(@PathVariable("itemId") String itemId) {
        return service.getItem(itemId);
    }
    
    @GetMapping(value = "all")
    public List<Map<String,Object>> getAll(){
        return service.getItems2();
    }
}