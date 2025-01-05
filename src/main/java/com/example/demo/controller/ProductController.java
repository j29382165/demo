package com.example.demo.controller;

import com.example.demo.constant.ProductCategory;
import com.example.demo.dto.ProductQueryParams;
import com.example.demo.dto.ProductRequest;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
public class ProductController {


    @Autowired
    private ProductService productService; //注入service的bean

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(
            // 查詢條件Filtering
          @RequestParam(required = false) ProductCategory category,
          @RequestParam(required = false) String search,

            //排序Sorting: OrderBy條件 , sort降冪排序 高到低
          @RequestParam(defaultValue="created_date") String orderBy,
          @RequestParam(defaultValue = "desc") String sort,

            //分頁pagination limit=取得幾筆商品數據，offset=跳過幾筆數據，對應sql語句LIMIT OFFSET
          @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
          @RequestParam(defaultValue = "0") @Min(0) Integer offset



    ){
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        List<Product>productList=productService.getProducts(productQueryParams);

        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId){
        Product product = productService.getProductById(productId);
        if (product != null){
            return ResponseEntity.status(HttpStatus.OK).body(product); // OK 200
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // NOT_FOUND 404
        }

    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
       Integer productId = productService.createProduct(productRequest);
       Product product = productService.getProductById(productId);
       return ResponseEntity.status(HttpStatus.CREATED).body(product); // CREATED 201
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                @RequestBody @Valid ProductRequest productRequest){
        //接住前端傳過來的參數:允許修改productName category imageUrl price stock description @RequestBody～

        //用productId檢查商品數據是否存在
        Product product=productService.getProductById(productId);

        if(product==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();//找不到商品回傳 NOT_FOUND 404
        }

        //商品存在，則修改商品的數據
        //productService提供update方法
        productService.updateProduct(productId,productRequest);

        Product updateProduct = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);

    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){
        //此功能是讓商品不存在,前端只要結果商品不存在，不在乎後端是否執行刪除，也有可能是商品本身不存在or後端刪除才不存在

        productService.deleteProductById(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // NO_CONTENT 204
        // NO_CONTENT 204表明請求已成功處理，但伺服器未傳回任何內容

    }


}
