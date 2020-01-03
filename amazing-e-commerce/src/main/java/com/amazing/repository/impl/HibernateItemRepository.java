package com.amazing.repository.impl;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.amazing.model.Item;
import com.amazing.repository.ItemRepository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * HibernateItemRepository
 */
@Repository
@Transactional
public class HibernateItemRepository implements ItemRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public HibernateItemRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(Item item) {
        currentSession().save(item);
    }

    @Override
    public Item findById(String id) {
        return currentSession().get(Item.class, id);
    }

    @Override
    public List<Item> findByTypeId(Integer typeId) {
        String hql = "from Item where typeId=:typeId";
        Query<Item> query = currentSession().createQuery(hql, Item.class);
        query.setParameter("typeId", typeId);
        return query.getResultList();
    }

    @Override
    public List<Item> findAll() {
        String hql = "from Item";
        Query<Item> query = currentSession().createQuery(hql, Item.class);
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> findAll2() {
        String sql = "select items.id, items.name, items.price, items.stock, items.info, item_types.name `type`, item_imgs.url "
                    + "from items, item_types, item_imgs where "
                    + "items.type_id=item_types.id and items.id=item_imgs.item_id";
        NativeQuery<?> query = currentSession().createSQLQuery(sql);
        List<Map<String,Object>> list = new LinkedList<>();
        Map<String,Object> map = null;
        for(Object row : query.getResultList()){
            map = new HashMap<>();
            map.put("id", Array.get(row,0));
            map.put("name", Array.get(row,1));
            map.put("price", Array.get(row,2));
            map.put("stock", Array.get(row, 3));
            map.put("info", Array.get(row, 4));
            map.put("type", Array.get(row, 5));
            map.put("imgUrl", Array.get(row, 6));
            list.add(map);
        }
        return list;
    }

    @Override
    public void remove(Item item) {
        currentSession().delete(item);
    }

    @Override
    public void update(Item item) {
        currentSession().update(item);

    }

    private Session currentSession() {
        try {
            return sessionFactory.getCurrentSession();
        } catch (Exception e) {
            e.printStackTrace();
            return sessionFactory.openSession();
        }
    }



}