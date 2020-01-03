package com.amazing.repository;

import com.amazing.model.User;

/**
 * UserRepository
 */

public interface UserRepository {

    void add(User user);
    User findByAccount(String account);
    User findByEmail(String email);
    void remove(User user);
    void update(User user);
}