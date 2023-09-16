package com.tcs.orderservice.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.tcs.orderservice.dto.Payment;
import com.tcs.orderservice.dto.PaymentKafka;
import com.tcs.orderservice.enums.OrderStatus;
import com.tcs.orderservice.enums.PaymentStatus;
import com.tcs.orderservice.model.Order;
import com.tcs.orderservice.repository.OrderRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.security.oauthbearer.internals.OAuthBearerClientInitialResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
public class OrderService
{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;


    @KafkaListener(topics = "paymentTopic",groupId = "br-group")
    public void paymentStatusEvent(ConsumerRecord pt) throws JsonProcessingException {
        PaymentKafka paymentKafka=new ObjectMapper().readValue(pt.value().toString(),PaymentKafka.class);
        System.out.println("Message consumed : "+paymentKafka);
        if(paymentKafka.getStatus().equals(PaymentStatus.SUCCESS))
        {
            this.ConfirmOrder(paymentKafka.getOrderId());
        }
        else
        {
            this.cancelOrder(paymentKafka.getOrderId());
        }
    }


    public Order createOrder(Order orderData)
    {

        Order order=this.orderRepository.save(orderData);
        Payment payment=new Payment(order.getId(), PaymentStatus.INITIATED);
        try
        {
            HttpStatusCode statusCode =restTemplate.postForEntity("http://127.0.0.1:8083/payment/init",payment,Object.class).getStatusCode();
            System.out.println(statusCode.toString());
            if(statusCode.is2xxSuccessful())
            {
                System.out.println("Payment initiated...");
            }
            else
            {
                System.out.println("Unable to initiate payment...");
            }
        }
        catch (Exception e)
        {
            System.out.println("Unable to initiate payment...");
        }

        return  order;

    }

    public Order cancelOrder(Long id)
    {
        Order order=this.orderRepository.findById(id).get();
        order.setStatus(OrderStatus.CANCELLED);
        return this.orderRepository.save(order);
    }

    public Order ConfirmOrder(Long id)
    {
        Order order=this.orderRepository.findById(id).get();
        order.setStatus(OrderStatus.SUCCESS);
        return this.orderRepository.save(order);
    }

    public Order getOrder(Long id)
    {
        return this.orderRepository.findById(id).get();
    }

    public List<Order> allOrder()
    {
        return this.orderRepository.findAll();
    }

}
