package com.amazing.service;

import com.amazing.model.User;
import com.amazing.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserService
 */
@Service
public class UserService {

    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository){
        this.repository = repository;
    }

    public boolean signup(User user){

        if (user == null) {
            return false;
        }

        if (user.getAccount() == null || user.getPassword() == null) {
            return false;
        }

        User ano = repository.findByAccount(user.getAccount());
        if(ano != null){
            return false;
        }else{
            repository.add(user);
            return true;
        }
    }

    public User login(User user){

        if(user == null){
            return null;
        }

        if(user.getAccount() == null || user.getPassword() == null){
            return null;
        }

        User realUser = repository.findByAccount(user.getAccount());
        if( realUser == null ){
            return null;
        }if( user.getPassword().equals( realUser.getAccount() ) ){
            return null;
        }
        else{
            return realUser;
        }
    }

    public boolean loginVerification(User user){
        return login(user) != null ? true : false;
    }

    public boolean change(User user){
        if( loginVerification(user) ){
            repository.update(user);
            return true;
        }else{
            return false;
        }
    }
}