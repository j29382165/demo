package com.example.demo.service;

import com.example.demo.dto.ProductRequest;
import com.example.demo.model.Product;

public interface ProductService {

    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);

}
