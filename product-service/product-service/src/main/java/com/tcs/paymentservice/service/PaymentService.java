package com.tcs.paymentservice.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.paymentservice.enums.PaymentStatus;
import com.tcs.paymentservice.model.Payment;
import com.tcs.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentService
{
    @Autowired
    private PaymentRepository paymentRepository;


    @Autowired
    KafkaTemplate<String,Object> template;

    public Payment processPayment(Payment payment) throws JsonProcessingException {
        Payment paymentResponse=null;
        try
        {
            System.out.println("processing");
            paymentResponse=this.paymentRepository.save(payment);
            this.successPayment(payment.getId());

        }
        catch(Exception e)
        {
            this.rejectPayment(payment.getId());
        }
        return paymentResponse;
    }

    public Payment rejectPayment(Long id) throws JsonProcessingException {
        Payment payment=this.paymentRepository.findById(id).get();
        payment.setStatus(PaymentStatus.FAILURE);
        ObjectMapper mapper=new ObjectMapper();
        CompletableFuture<SendResult<String,Object>> response= template.send("paymentTopic",mapper.writeValueAsString(payment));
        response.whenComplete((result,ex)->{
            if (ex == null) {
                System.out.println("Sent message with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message due to : " + ex.getMessage());
            }
        });
        return this.paymentRepository.save(payment);
    }

    public Payment successPayment(Long id) throws JsonProcessingException {
        Payment payment=this.paymentRepository.findById(id).get();
        payment.setStatus(PaymentStatus.SUCCESS);
        ObjectMapper mapper=new ObjectMapper();
        CompletableFuture<SendResult<String,Object>> response= template.send("paymentTopic",mapper.writeValueAsString(payment));
        response.whenComplete((result,ex)->{
            if (ex == null) {
                System.out.println("Sent message with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message due to : " + ex.getMessage());
            }
        });
        return this.paymentRepository.save(payment);
    }

    public Payment getPayment(Long id)
    {
        return this.paymentRepository.findById(id).get();
    }

    public List<Payment> allPayments()
    {
        return this.paymentRepository.findAll();
    }

}
