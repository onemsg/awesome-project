package com.amazing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * Item
 */
@Data
@Entity
@Table(name = "items")
public class Item {

    @Id
    private String id;
    private String name;
    private Float price;
    private Integer stock;
    private String info;
    @Column(name = "type_id")
    private Integer typeId;
}