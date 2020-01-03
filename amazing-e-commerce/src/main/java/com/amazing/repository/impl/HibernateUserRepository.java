package com.amazing.repository.impl;

import javax.transaction.Transactional;

import com.amazing.model.User;
import com.amazing.repository.UserRepository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * HibernateUserRepository
 */
@Repository
@Transactional
public class HibernateUserRepository implements UserRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public HibernateUserRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(User user) {
        currentSession().save(user);

    }

    @Override
    public User findByAccount(String account) {
        return currentSession().find(User.class, account);
    }

    @Override
    public User findByEmail(String email) {
        String hql = "from User where email=:email";
        Query<User> query =  currentSession().createNamedQuery(hql, User.class);
        query.setParameter("email", email);
        return query.getSingleResult();     
    }

    @Override
    public void remove(User user) {
        currentSession().delete(user);

    }

    @Override
    public void update(User user) {
        currentSession().update(user);

    }

    public void changePassword(User user, String newpassword){
        String sql = "update users set password=:password where account=:account";
        NativeQuery<?> query = currentSession().createNativeQuery(sql);
        query.setParameter("password", newpassword);
        query.setParameter("account", user.getAccount());
        query.executeUpdate();
    }

    private Session currentSession(){
        try{
            return sessionFactory.getCurrentSession();
        }catch(Exception e){
            e.printStackTrace();
            return sessionFactory.openSession();
        }
        
    }
}