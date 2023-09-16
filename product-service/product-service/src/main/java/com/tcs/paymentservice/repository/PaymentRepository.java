package com.tcs.paymentservice.repository;

import com.tcs.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long>
{
}
