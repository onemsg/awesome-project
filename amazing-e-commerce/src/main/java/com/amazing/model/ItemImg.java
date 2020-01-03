package com.amazing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
/**
 * ItemImg
 */
@Data
@Entity
@Table(name = "item_imgs")
public class ItemImg {

    @Id
    private Integer id;
    private String name;
    private String url;
    @Column(name = "item_id")
    private String itemId;
}