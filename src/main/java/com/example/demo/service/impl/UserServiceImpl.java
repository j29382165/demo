package com.example.demo.service.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.dto.UserLoginRequest;
import com.example.demo.dto.UserRegisterRequest;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;

@Component
public class UserServiceImpl implements UserService { //加Component,讓UserServiceImpl成為Bean
    //制式寫法log slf4j ,換掉class名稱即可
    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        //檢查著的email,確認email唯一性
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        if (user != null) {
            log.warn("該email:{}已被註冊", userRegisterRequest.getEmail());//{}大括號放參數
            //log.warn("該email{}已被{}註冊",userRegisterRequest.getEmail(),"Linda");//{}大括號放參數
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //BAD_REQUEST 400
        }

        //使用MD5加密密碼，生成雜湊
        String hashedPassword= DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);
        //創建帳號
        return userDao.createUser(userRegisterRequest);

        //controller層、service層用register,dao層用createUser,有講究的(?
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());
        if (user==null){
            log.warn("該email:{}尚未被註冊",userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //強制停止前端請求400 BAD_REQUEST
        }

        String hashedPassword= DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        if(user.getPassword().equals(hashedPassword)){ //String比較要用equals,非==
            return user;
        }
        else{
            log.warn("email{}的密碼錯誤",userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //強制停止前端請求400 BAD_REQUEST
        }
    }




}
