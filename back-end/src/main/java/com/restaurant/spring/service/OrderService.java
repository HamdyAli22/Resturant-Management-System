package com.restaurant.spring.service;

import com.restaurant.spring.controller.vm.RequestOrderVm;
import com.restaurant.spring.controller.vm.ResponseOrderVm;
import com.restaurant.spring.controller.vm.UserOrderResponseVm;
import com.restaurant.spring.dto.OrderDto;
import com.restaurant.spring.model.Order;

import java.util.List;

public interface OrderService {
    ResponseOrderVm requestOrder(RequestOrderVm requestOrderVm);
    UserOrderResponseVm getUserOrders(int page,int size);
    UserOrderResponseVm getAllOrders(int page,int size,String username);
    OrderDto getOrderById(Long id);
    void deleteOrder(Long id);
    UserOrderResponseVm searchUserOrders(String keyword,int page,int size);
}
