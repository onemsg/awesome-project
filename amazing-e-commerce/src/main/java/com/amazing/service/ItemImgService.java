package com.amazing.service;

import java.util.List;

import com.amazing.model.ItemImg;
import com.amazing.repository.ItemImgRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ItemImgImgService
 */
@Service
public class ItemImgService {

    private ItemImgRepository repository;

    @Autowired
    public ItemImgService(ItemImgRepository repository){
        this.repository = repository;
    }

    public List<ItemImg> getItemImgs(String itemId){
        return repository.findByItemId(itemId);
    }

}