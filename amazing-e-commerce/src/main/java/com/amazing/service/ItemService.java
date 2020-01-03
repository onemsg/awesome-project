package com.amazing.service;

import java.util.List;
import java.util.Map;

import com.amazing.model.Item;
import com.amazing.repository.ItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ItemService
 */
@Service
public class ItemService {

    private ItemRepository repository;

    @Autowired
    public ItemService(ItemRepository repository){
        this.repository = repository;
    }

    public List<Item> getItemsOfType(Integer typeId){
        return repository.findByTypeId(typeId);
    }

    public Item getItem(String id){
        return repository.findById(id);
    }

    public List<Item> getItems(){
        return repository.findAll();
    }

    public List<Map<String, Object>> getItems2(){
        List<Map<String, Object>> list =  repository.findAll2();
        list.forEach( map -> map.put("imgUrl", "images/" + map.get("imgUrl")) );
        return list;
    }

    public boolean addItem(Item item){
        Item ano = repository.findById(item.getId());
        if( ano != null ){
            return false;
        }else{
            repository.add(item);
            return true;
        }
    }

    public void removeItem(Item item){
        Item ano = repository.findById(item.getId());
        if (ano != null) {
            repository.remove(item);
        }
    }

    public boolean changeItem(Item item){
        Item ano = repository.findById(item.getId());
        if (ano == null){
            return false;
        }else{
            repository.update(item);
            return true;
        }

    }

}