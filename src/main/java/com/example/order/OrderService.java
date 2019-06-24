package com.example.order;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;



  /*  @RabbitListener(queues = "order")
    @SendTo("reply_queue")
    public  boolean setOrder (String value,Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        System.out.println("Got request "+ value);
        if(value.equals("true")){
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setId(UUID.randomUUID());
            orderEntity.setOrderState(OrderState.APPROVED);
            orderRepository.save(orderEntity);
            channel.basicAck(tag,false);
            System.out.println("Sent response ");
            return true;
        }

       return false;
    }*/



    @RabbitListener(queues = "order")
    @SendTo("reply_queue")
    public  String setOrder (String str,Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        ObjectMapper objectMapper= new ObjectMapper();
        OrderEntity orderEntity=objectMapper.readValue(str,OrderEntity.class);
        System.out.println("Got request "+ orderEntity);

            orderEntity.setId(UUID.randomUUID());
            orderRepository.save(orderEntity);
            channel.basicAck(tag,false);
            System.out.println("Sent response ");
            return str;

    }




}
