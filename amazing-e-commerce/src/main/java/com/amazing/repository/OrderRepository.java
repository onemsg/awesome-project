package com.amazing.repository;

import com.amazing.model.Order;
import java.util.List;

/**
 * OrderRepository
 */
public interface OrderRepository {

    void add(Order order);
    List<Order> findByAccount();
    void remove(Order order);
    void update(Order order);
}