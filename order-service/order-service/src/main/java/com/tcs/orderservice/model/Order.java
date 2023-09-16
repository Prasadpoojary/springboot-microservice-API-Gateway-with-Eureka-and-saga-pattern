package com.tcs.orderservice.model;


import com.tcs.orderservice.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double price;

    private OrderStatus status;
}
