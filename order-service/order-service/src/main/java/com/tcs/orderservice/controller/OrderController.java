package com.tcs.orderservice.controller;


import com.tcs.orderservice.model.Order;
import com.tcs.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController
{
    @Autowired
    private OrderService orderService;

    @PostMapping("/init")
    public ResponseEntity<Order> makeOrder(@RequestBody Order orderRequest)
    {
        Order order = this.orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id)
    {
        Order order = this.orderService.cancelOrder(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PatchMapping("/process/{id}")
    public ResponseEntity<Order> processOrder(@PathVariable Long id)
    {
        Order order = this.orderService.ConfirmOrder(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id)
    {
        Order order = this.orderService.getOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> allOrder()
    {
        List<Order> orders = this.orderService.allOrder();
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }
}
