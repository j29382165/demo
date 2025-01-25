package com.example.demo.service.impl;

import com.example.demo.dao.OrderDao;
import com.example.demo.dto.BuyItem;
import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.demo.dao.ProductDao;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

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
        int totalAmount=0;
        List<OrderItem> orderItemList= new ArrayList<>();


        for(BuyItem buyItem : createOrderRequest.getBuyItemList()){ //for loop
            Product product = productDao.getProductById(buyItem.getProductId());
            //計算總價錢
            int amount = buyItem.getQuantity()*product.getPrice();
            totalAmount=totalAmount+amount;

            //轉換BuyItem to OrderItem
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
