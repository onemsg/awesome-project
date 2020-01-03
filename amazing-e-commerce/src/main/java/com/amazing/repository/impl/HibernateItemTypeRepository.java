package com.amazing.repository.impl;

import java.util.List;

import javax.transaction.Transactional;

import com.amazing.model.ItemType;
import com.amazing.repository.ItemTypeRepository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * HibernateItemTypeRepository
 */
@Repository
@Transactional
public class HibernateItemTypeRepository implements ItemTypeRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public HibernateItemTypeRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(ItemType ItemType) {
        
        currentSession().save(ItemType);
    }

    @Override
    public ItemType findById(Integer id) {
        return currentSession().get(ItemType.class, id);
    }

    @Override
    public ItemType findByName(String name) {
        String hql = "from ItemType where name=:name";
        Query<ItemType> query = currentSession().createQuery(hql, ItemType.class);
        return query.getSingleResult();
    }

    @Override
    public List<ItemType> findAll() {
        return currentSession().createQuery("from ItemType", ItemType.class).getResultList();
    }
    
    @Override
    public void update(ItemType ItemType) {
        currentSession().update(ItemType);

    }

    @Override
    public void remove(ItemType ItemType) {
        currentSession().delete(ItemType);

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