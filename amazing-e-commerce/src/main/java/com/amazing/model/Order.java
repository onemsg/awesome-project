package com.amazing.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
/**
 * Order
 */
@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String id;
    @Column(nullable = false)
    private String account;
    @Column(name = "item_id")
    private String itemId;
    private Integer count;
    private Date date;
    @Column(name = "address_id")
    private Integer addressId;
    private boolean ispayed;
}