package com.amazing.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.amazing.model.User;
import com.amazing.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


 
@RestController
@RequestMapping(value = "/api/user")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
    private static final String CURRENT_USER_SESSION_KEY = "currentUserSessionKey";

    @Autowired
    private UserService service;

    @PostMapping(value="/singup")
    public Map<String,Object> singup(@RequestBody User user, HttpSession session){

        boolean falg = service.signup(user);
        Map<String,Object> map = new HashMap<>();
        map.put("state", falg);
        if (falg){
            map.put("message", "账号注册成功");
            user.setPassword("");
            session.setAttribute(CURRENT_USER_SESSION_KEY, user);
            logger.info("user {} 注册成功", user);
        }else{
            map.put("message", "账号注册失败");
            logger.info("user {} 注册失败", user);
        }
        return map;
        
    }

    @PostMapping(value = "/login")
    public Map<String,Object> login(@RequestBody(required = false) User user, HttpSession session){
        
        if(user == null){
            logger.info("request传入的 user 为空");
        }
        User realUser = service.login(user);
        boolean falg = realUser != null;

        Map<String, Object> map = new HashMap<>();
        map.put("state", falg);
        if (falg) {
            map.put("message", "账号验证成功");
            realUser.setPassword("");
            session.setAttribute(CURRENT_USER_SESSION_KEY, realUser);
            logger.info("user {} 登陆成功", realUser);
        } else {
            map.put("message", "账号验证失败");
            logger.info("user {} 登陆失败", realUser);
        }
        return map;
    }

    @GetMapping(value="/logout")
    public Map<String,Object> logout( HttpSession session) {
        
        User user = (User) session.getAttribute(CURRENT_USER_SESSION_KEY);
        boolean falg = user != null;
        
        Map<String, Object> map = new HashMap<>();
        map.put("state", falg);
        if (falg) {
            map.put("message", "账号退出成功");
            session.removeAttribute(CURRENT_USER_SESSION_KEY);
            logger.info("user {} 已退出", user);
        } else {
            map.put("message", "未登陆，退出个毛");
            logger.info("user {} 未登陆，退出个毛", user);
        }
        return map;
    }
    
    @GetMapping(value = "/isLogin")
    public Map<String,Object> isLogin( HttpSession session ){

        logger.info("Session ID:{} 验证用户是否登陆", session.getId());

        User user = (User) session.getAttribute(CURRENT_USER_SESSION_KEY);
        Map<String,Object> map = new HashMap<>();
        if (user == null){
            map.put("state", false);
            map.put("message", "登陆个球");
        } else{
            map.put("state", true);
            map.put("message", "这货登陆了，前端伙计");
        }
        return map;
    }

    @GetMapping(value = "/info")
    public Map<String, Object> getMethodName(HttpSession session) {

        logger.info("Session ID:{} 获取用户信息", session.getId());

        User user = (User) session.getAttribute(CURRENT_USER_SESSION_KEY);
        Map<String, Object> map = new HashMap<>();
        if (user == null) {
            map.put("state", false);
            map.put("message", "没登陆");
        } else {
            map.put("state", true);
            map.put("message", "好了，给用户信息");
            map.put("user", user);
        }
        return map;
    }
    
}