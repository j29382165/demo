package com.example.demo.dao;

import com.example.demo.dto.UserRegisterRequest;
import com.example.demo.model.User;

import java.util.List;

public interface UserDao {
    Integer createUser(UserRegisterRequest userRegisterRequest);
    User getUserByEmail(String email);
    User getUserById(Integer userId);

    //整批加密之前明碼,執行過就關閉
//    List<User> getAllUsers();
//    void updateUserPassword(Integer userId, String hashedPassword);

}
