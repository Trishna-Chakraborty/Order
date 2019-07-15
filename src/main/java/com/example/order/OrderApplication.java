package com.example.order;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class OrderApplication {


    @Bean
    Queue postOrderQueue() {

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-dead-letter-exchange", "dead_exchange");
        args.put("x-message-ttl", 60000);
        //args.put("x-dead-letter-routing-key","post.#");
        return new Queue("postOrder", false, false, false, args);
    }


    @Bean
    Queue updateOrderQueue() {

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-dead-letter-exchange", "dead_exchange");
        args.put("x-message-ttl", 60000);
        //args.put("x-dead-letter-routing-key","post.#");
        return new Queue("updateOrder", false, false, false, args);
    }

    @Bean
    Queue postTransactionQueue() {

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-dead-letter-exchange", "dead_exchange");
        args.put("x-message-ttl", 60000);
        //args.put("x-dead-letter-routing-key","post.#");
        return new Queue("postTransaction", false, false, false, args);
    }
    @Bean
    DirectExchange orderExchange() {
        return new DirectExchange("order_exchange");
    }
    @Bean
    DirectExchange transactionExchange() {
        return new DirectExchange("transaction_exchange");
    }

    @Bean
    Binding postTransactionBinding(Queue postTransactionQueue, DirectExchange transactionExchange) {
        return BindingBuilder.bind(postTransactionQueue).to(transactionExchange).with("post.transaction");
    }
    @Bean
    Binding updateOrderBinding(Queue updateOrderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(updateOrderQueue).to(orderExchange).with("update.order");
    }
    @Bean
    Binding postOrderBinding(Queue postOrderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(postOrderQueue).to(orderExchange).with("post.order");
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
