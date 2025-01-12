package com.example.demo.controller;

import com.example.demo.dto.UserLoginRequest;
import com.example.demo.dto.UserRegisterRequest;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //實作註冊新帳號的api功能,post方法建立user資料
    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){ //接住前端傳來的參數
       Integer userId= userService.register(userRegisterRequest);  //userService提供註冊的方法
       User user= userService.getUserById(userId);

       return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    //實作登入的api功能,post方法
    @PostMapping("/users/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest userLoginRequest){
        User user= userService.login(userLoginRequest);  //userService提供登入的方法

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


}
