package com.amazing.repository.impl;

import java.util.List;

import javax.transaction.Transactional;

import com.amazing.model.ItemImg;
import com.amazing.repository.ItemImgRepository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * HibernateItemImgRepository
 */
@Repository
@Transactional
public class HibernateItemImgRepository implements ItemImgRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public HibernateItemImgRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(ItemImg itemImg) {
        currentSession().save(itemImg);

    }

    @Override
    public ItemImg findById(Integer id) {
        return currentSession().get(ItemImg.class, id);
    }

    @Override
    public List<ItemImg> findByItemId(String itemId) {
        String hql = "from ItemImg where itemId=:itemId";
        Query<ItemImg> query = currentSession().createNamedQuery(hql, ItemImg.class);
        query.setParameter("itemId", itemId);
        return query.getResultList();
    }

    @Override
    public List<ItemImg> findAll() {
        String hql = "from ItemImg";
        Query<ItemImg> query = currentSession().createQuery(hql, ItemImg.class);
        return query.getResultList();
    }

    @Override
    public void update(ItemImg itemImg) {
        currentSession().update(itemImg);
    }

    @Override
    public void remove(ItemImg itemImg) {
        currentSession().delete(itemImg);
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