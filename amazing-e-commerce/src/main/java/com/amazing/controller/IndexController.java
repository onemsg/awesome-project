package com.amazing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * IndeController
 */
@Controller
public class IndexController {

    
    @GetMapping(value = {"/", "index","home"})
    public String index() {
        return "home";
    }

    @GetMapping(value = "items")
    public String items() {
        return "items";
    }

    @GetMapping(value = "login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "orders")
    public String orders() {
        return "orders";
    }
}