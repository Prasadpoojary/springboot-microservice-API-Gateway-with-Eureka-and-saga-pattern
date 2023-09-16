package com.tcs.paymentservice.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.tcs.paymentservice.model.Payment;
import com.tcs.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController
{
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/init")
    public ResponseEntity<Payment> initiatePayment(@RequestBody Payment paymentRequest) throws JsonProcessingException {
        Payment payment = this.paymentService.processPayment(paymentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<Payment> cancelPayment(@PathVariable Long id) throws JsonProcessingException {
        Payment payment = this.paymentService.rejectPayment(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @PatchMapping("/success/{id}")
    public ResponseEntity<Payment> successPayment(@PathVariable Long id) throws JsonProcessingException {
        Payment payment = this.paymentService.successPayment(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id)
    {
        Payment payment = this.paymentService.getPayment(id);
        return ResponseEntity.status(HttpStatus.OK).body(payment);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Payment>> allPayments()
    {
        List<Payment> payments = this.paymentService.allPayments();
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }
}
