package com.example.demo.dao;

import com.example.demo.model.Product;

public interface ProductDao { //product類型的返回方法
    Product getProductById(Integer productId);
}
