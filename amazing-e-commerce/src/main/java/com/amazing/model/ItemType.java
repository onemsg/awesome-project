package com.amazing.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
/**
 * ItemType
 */
@Data
@Entity
@Table(name = "item_types")
public class ItemType {
    
    @Id
    private Integer id;
    private String name;
    private String info;
}