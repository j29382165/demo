package com.example.demo.service.impl;

import com.example.demo.dao.OrderDao;
import com.example.demo.dto.BuyItem;
import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.OrderQueryParams;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.example.demo.dao.ProductDao;
import com.example.demo.dao.UserDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class OrderServiceImpl implements OrderService {
    //制式寫法log slf4j ,換掉class名稱即可
    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        return orderDao.countOrder(orderQueryParams);
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        List<Order> orderList = orderDao.getOrders(orderQueryParams);

        for(Order order : orderList){
            List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(order.getOrderId());
            order.setOrderItemList(orderItemList);
        }
        return orderList;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        // order table
        Order order = orderDao.getOrderById(orderId);

        // order item table
        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

        order.setOrderItemList(orderItemList);
        return order;
    }

    //service層 商業邏輯判斷
    @Transactional //Order table、order item table都一起成功或一起失敗,避免數據不一致的問題
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        // 檢查user是否存在
        User user =userDao.getUserById(userId);
        if(user==null){
            log.warn("該userId{}不存在",userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); // BAD_REQUEST 400

        }


        int totalAmount=0;
        List<OrderItem> orderItemList= new ArrayList<>();


        for(BuyItem buyItem : createOrderRequest.getBuyItemList()){ //for loop
            Product product = productDao.getProductById(buyItem.getProductId());
            // 檢查product是否存在
            if(product==null){
                log.warn("商品{}不存在",buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST); // BAD_REQUEST 400
            }else if(product.getStock()< buyItem.getQuantity()){
                // 檢查商品庫存是否足夠
                log.warn("商品{}庫存不足，無法購買。剩餘多少庫存{}，您欲購買數量{}",
                        buyItem.getProductId(),product.getStock(),buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST); // BAD_REQUEST 400

            }else {
                //扣除商品庫存
                productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());
            }




            // 計算總價錢
            int amount = buyItem.getQuantity()*product.getPrice();
            totalAmount=totalAmount+amount;

            // 轉換BuyItem to OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);


        }


        // 創建訂單
        Integer orderId= orderDao.createOrder(userId,totalAmount); //訂單，call dao一次

        orderDao.createOrderItems(orderId, orderItemList); //訂單細項，call dao第二次

        return orderId;

    }


}
