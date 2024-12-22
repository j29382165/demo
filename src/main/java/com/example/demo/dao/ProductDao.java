package com.example.demo.dao;

import com.example.demo.dto.ProductRequest;
import com.example.demo.model.Product;

public interface ProductDao { //product類型的返回方法
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);

}
