package com.example.demo.service;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.OrderQueryParams;
import com.example.demo.model.Order;

import java.util.List;

public interface OrderService {
    Order getOrderById(Integer orderId);

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    //取得order list
    List<Order> getOrders(OrderQueryParams orderQueryParams);

    //取得order總數
    Integer countOrder (OrderQueryParams orderQueryParams);
}
