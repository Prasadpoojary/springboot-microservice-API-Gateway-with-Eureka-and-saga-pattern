package com.tcs.paymentservice.model;


import com.tcs.paymentservice.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private PaymentStatus status;
}
