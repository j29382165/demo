package com.example.demo.dao;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.OrderQueryParams;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer createOrder(Integer userId, Integer totalAmount);

    void createOrderItems (Integer orderId, List<OrderItem> orderItemList);

    Order getOrderById(Integer orderId);

    List<OrderItem> getOrderItemsByOrderId (Integer orderId);

    //取得order list
    List<Order> getOrders(OrderQueryParams orderQueryParams);

    //取得order總數
    Integer countOrder (OrderQueryParams orderQueryParams);

}
