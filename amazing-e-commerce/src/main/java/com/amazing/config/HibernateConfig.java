package com.amazing.config;

import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@PropertySource(value = "classpath:/application.properties")
@EnableTransactionManagement
public class HibernateConfig {

    @Autowired
    private Environment env;

    @Bean(destroyMethod = "close")
    public BasicDataSource dataSource(){
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        ds.setUrl(env.getProperty("spring.datasource.url"));
        ds.setUsername(env.getProperty("spring.datasource.username"));
        ds.setPassword(env.getProperty("spring.datasource.password"));
        ds.setInitialSize(5);
        return ds;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactoryBean(){
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "com.amazing.model" });
        Properties pros = new Properties();
        pros.setProperty("hibernate.dialect","org.hibernate.dialect.MySQLDialect");
        sessionFactory.setHibernateProperties(pros);
        return sessionFactory;
    }    

    @Bean
    public HibernateTransactionManager hibernateTransactionManager(SessionFactory sessionFactory){
        return new HibernateTransactionManager(sessionFactory);
    }


    // @Bean
    // public LocalContainerEntityManagerFactoryBean entityManagerFactory(
    //         DataSource dataSource, JpaVendorAdapter jpaVendorAdapter){
    //     LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
    //     emfb.setDataSource(dataSource);
    //     emfb.setJpaVendorAdapter(jpaVendorAdapter);
    //     emfb.setPackagesToScan("com.amazing.model");
    //     EntityManagerFactory emf = emfb.getNativeEntityManagerFactory();
    //     emf.createEntityManager().
    //     return emfb;

    // }
    
    // @Bean
    // public JpaVendorAdapter jpaVendorAdapter(){
    //     HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    //     adapter.setDatabase(Database.MYSQL);
    //     adapter.setShowSql(true);
    //     adapter.setGenerateDdl(false);
    //     adapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
    //     return adapter;
    // }

    
}