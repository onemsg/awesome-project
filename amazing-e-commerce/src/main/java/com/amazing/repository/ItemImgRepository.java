package com.amazing.repository;

import java.util.List;

import com.amazing.model.ItemImg;

/**
 * ItemImgRepository
 */
public interface ItemImgRepository {

    void add(ItemImg itemImg);

    ItemImg findById(Integer id);

    List<ItemImg> findByItemId(String itemId);

    List<ItemImg> findAll();
    void update(ItemImg itemImg);

    void remove(ItemImg itemImg);
}