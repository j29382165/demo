package com.example.demo.dao.impl;

import com.example.demo.dao.ProductDao;
import com.example.demo.dto.ProductQueryParams;
import com.example.demo.dto.ProductRequest;
import com.example.demo.model.Product;
import com.example.demo.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductDaoImpl implements ProductDao { //成為Bean

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams){

        String sql = "SELECT product_id, product_name, category, " +
                "image_url, price, stock, description, created_date, " +
                "last_modified_date FROM product WHERE 1=1";


        Map<String, Object> map = new LinkedHashMap<>(); //空的map

        //查詢條件category
        if(productQueryParams.getCategory() !=null){
            sql=sql+" AND category = :category"; // AND前方空白鍵一定要留，才不會跟前面的語句黏在一起
            map.put("category",productQueryParams.getCategory().name()); //category是enum類型，可使用enum的name方法
        }

        //查詢條件search
        if(productQueryParams.getSearch() !=null){
            sql=sql+" AND product_name LIKE :search"; //:search占位符，:search 參數化查詢，可以有效避免 SQL 注入攻擊。
            map.put("search","%"+productQueryParams.getSearch()+"%"); //AND...LIKE %+search+%模糊搜尋
        }

        //查詢條件排序orderBy sort升冪降冪
        sql=sql+" ORDER BY "+ productQueryParams.getOrderBy()+" "+ productQueryParams.getSort();
        // 寫sql語句注意空白，才不會全部黏在一起不能執行

        //避免 SQL注入風險
        List<String> validColumns = Arrays.asList("price", "created_date", "stock");
        if (!validColumns.contains(productQueryParams.getOrderBy())) {
            throw new IllegalArgumentException("Invalid orderBy parameter");
        }

        //查詢條件limit offset
        sql=sql+ " LIMIT :limit OFFSET :offset";
        map.put("limit",productQueryParams.getLimit()); //把其前端傳來的值放map
        map.put("offset",productQueryParams.getOffset()); //把其前端傳來的值放map

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
