package com.example.demo.service;

import com.example.demo.dto.UserLoginRequest;
import com.example.demo.dto.UserRegisterRequest;
import com.example.demo.model.Product;
import com.example.demo.model.User;

public interface UserService {
    Integer register(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
    User login(UserLoginRequest userLoginRequest);

    //整批加密之前明碼,執行過就關閉
//    void updateAllUserPasswords();
}
