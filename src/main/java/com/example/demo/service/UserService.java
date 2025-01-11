package com.example.demo.service;

import com.example.demo.dto.UserRegisterRequest;
import com.example.demo.model.Product;
import com.example.demo.model.User;

public interface UserService {
    Integer register(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);

}
