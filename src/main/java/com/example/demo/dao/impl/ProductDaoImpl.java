package com.example.demo.dao.impl;

import com.example.demo.dao.ProductDao;
import com.example.demo.model.Product;
import com.example.demo.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao { //成為Bean

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Product getProductById(Integer productId) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, " +
                "created_date, last_modified_date " +
                "FROM product WHERE product_id= :productId";

        Map<String, Object> map= new LinkedHashMap<>();
        map.put("productId",productId);
        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        if (productList.size()>0){
            return productList.get(0);
        }
        else {
            return null;
        }
    }
}
