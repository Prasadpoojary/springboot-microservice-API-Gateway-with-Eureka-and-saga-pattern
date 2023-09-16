package com.tcs.orderservice.dto;

import com.tcs.orderservice.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    private Long orderId;

    private PaymentStatus status;
}
