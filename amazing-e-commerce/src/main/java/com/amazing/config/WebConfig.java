package com.amazing.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * WebConfig
 */
@Configuration
@EnableWebMvc
@ComponentScan(value = "com.amazing.controller")
public class WebConfig implements WebMvcConfigurer{

    @Bean
    public ViewResolver viewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/views/");
        resolver.setSuffix(".html");
        resolver.setContentType("text/html; charset=utf-8");
        return resolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer ){
        configurer.enable();
    }

    // @Bean
    // public LocaleResolver localeResolver() {
    //     SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
    //     // Locale locale = new Locale("zh");
    //     sessionLocaleResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
    //     return sessionLocaleResolver;
    // }


}