package com.example.demo.dao.impl;

import com.example.demo.constant.ProductCategory;
import com.example.demo.dao.ProductDao;
import com.example.demo.dto.ProductRequest;
import com.example.demo.model.Product;
import com.example.demo.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao { //成為Bean

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Product> getProducts(ProductCategory category){

        String sql = "SELECT product_id, product_name, category, " +
                "image_url, price, stock, description, created_date, " +
                "last_modified_date FROM product WHERE 1=1";
        //

        Map<String, Object> map = new LinkedHashMap<>(); //空的map

        if(category !=null){
            sql=sql+" AND category = :category"; // AND前方空白鍵一定要留，才不會跟前面的語句黏在一起
            map.put("category",category.name()); //category是enum類型，可使用enum的name方法
        }

        List<Product> productList=namedParameterJdbcTemplate.query(sql,map,new ProductRowMapper()); //資料庫查詢出來的放到map裡

        return productList;


    }



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

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql="INSERT INTO product (product_name, category, image_url, price, stock," +
                " description, created_date, last_modified_date) " +
                "VALUES (:productName, :category, :imageUrl, :price, :stock, :description," +
                " :createDate, :lastModifiedDate);";

        //儲存前端傳過來的參數
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("productName",productRequest.getProductName());
        map.put("category",productRequest.getCategory().toString()); //.toString()要寫
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());

        //記錄變動時間
        Date now = new Date();
        map.put("createDate",now);
        map.put("lastModifiedDate",now);

        //keyHolder儲存資料庫自動生成的productId
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map),keyHolder);

        int productId =keyHolder.getKey().intValue();

        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        //前端傳過來的參數:允許修改productName category imageUrl price stock description,修改時間的值
        //寫到資料庫
        String sql ="UPDATE product SET product_name=:productName, category=:category, " +
                "image_url=:imageUrl,price=:price,stock=:stock,description=:description," +
                "last_modified_date=:lastModifiedDate WHERE product_id=:productId";

        Map<String,Object>map=new LinkedHashMap<>();
        map.put("productId",productId);
        map.put("productName",productRequest.getProductName());
        map.put("category",productRequest.getCategory().toString());
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());

        map.put("lastModifiedDate",new Date());
        namedParameterJdbcTemplate.update(sql,map);
    }

    @Override
    public void deleteProductById(Integer productId) {
        String sql="DELETE FROM product WHERE product_id=:productId";

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("productId",productId);

        namedParameterJdbcTemplate.update(sql,map);

    }
}
