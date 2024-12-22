package com.example.demo.rowmapper;

import com.example.demo.constant.ProductCategory;
import com.example.demo.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet resultSet, int i) throws SQLException {
       // 實作mapRow方法
        Product product =  new Product(); //ResultSet資料庫傳來的資料存在Product
        product.setProductId(resultSet.getInt("product_Id"));
        product.setProductName(resultSet.getString("product_name"));

//        String categoryStr=resultSet.getString("category");//資料庫傳來的文字
//        ProductCategory category = ProductCategory.valueOf(categoryStr);//把文字轉成enum類型
//        product.setCategory(category);
//上三行簡寫成以下一行
        product.setCategory(ProductCategory.valueOf(resultSet.getString("category")));

        product.setImageUrl(resultSet.getString("image_url"));
        product.setPrice(resultSet.getInt("price"));
        product.setStock(resultSet.getInt("stock"));
        product.setDescription(resultSet.getString("description"));
        product.setCreateDate(resultSet.getTimestamp("created_date"));
        product.setLastModifiedDate(resultSet.getTimestamp("last_modified_date"));
        return product;
    }
}
