package com.amazing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * User
 */
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    private String account;

    @Column(nullable = false)
    private String password;
    
    private String name;
    private String email;

    @Column(name = "default_address_id")
    private int defaultAdressId;
    private boolean isvip;

    public User(){ }


}