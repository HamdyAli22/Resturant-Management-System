package com.restaurant.spring.controller;

import com.restaurant.spring.controller.vm.RequestOrderVm;
import com.restaurant.spring.controller.vm.ResponseOrderVm;
import com.restaurant.spring.controller.vm.UserOrderResponseVm;
import com.restaurant.spring.dto.CategoryDto;
import com.restaurant.spring.dto.OrderDto;
import com.restaurant.spring.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create-orders")
    public ResponseEntity<ResponseOrderVm> createOrder(@RequestBody @Valid RequestOrderVm requestOrderVm) {
        return ResponseEntity.created(URI.create("create-orders")).body(orderService.requestOrder(requestOrderVm));
    }

    @GetMapping("/user-orders")
    public ResponseEntity<UserOrderResponseVm> getUserOrders() {
        UserOrderResponseVm orders = orderService.getUserOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/all-orders")
    public ResponseEntity<UserOrderResponseVm> getAllOrders(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String username) {
        UserOrderResponseVm allOrders = orderService.getAllOrders(page,size,username);
        return ResponseEntity.ok(allOrders);
    }

    @GetMapping("/get-order")
    public ResponseEntity<OrderDto> getOrderById(@RequestParam Long id) {
        OrderDto order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/delete-order")
    public ResponseEntity<Void> deleteOrder(@RequestParam Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }


}
