package com.amazing;

import java.util.List;
import java.util.Map;

import com.amazing.repository.ItemRepository;
// import com.amazing.repository.impl.HibernateItemRepository;
import com.amazing.service.ItemService;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 * TransactionTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = com.amazing.config.RootConfig.class)
// @ComponentScan(basePackages = { "com.amazing.repository" })
@PropertySource(value = "classpath:/application.properties")
public class DBTest {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemService itemService;

    @Test
    public void testDB(){
        List<Map<String,Object>> list = itemService.getItems2();
        for( Map<String,Object> map : list){
            System.out.println(map);
        }
    }


}