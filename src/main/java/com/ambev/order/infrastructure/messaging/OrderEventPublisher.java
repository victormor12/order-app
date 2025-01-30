package com.ambev.order.infrastructure.messaging;

import com.ambev.order.domain.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public OrderEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderToQueue(UUID orderId) {
        rabbitTemplate.convertAndSend("orders.queue", orderId);
    }

    public void sendProcessedOrder(Order order) {
        rabbitTemplate.convertAndSend("processed-orders.queue", order);
    }
}
