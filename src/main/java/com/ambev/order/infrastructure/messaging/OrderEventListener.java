package com.ambev.order.infrastructure.messaging;

import com.ambev.order.domain.model.Order;
import com.ambev.order.domain.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class OrderEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventListener.class);

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    public OrderEventListener(OrderRepository orderRepository, OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderEventPublisher = orderEventPublisher;
    }

    @Transactional
    @RabbitListener(queues = "orders.queue")
    public void processOrder(String orderId) {
        try {
            UUID id = UUID.fromString(orderId);
            Order order = fetchOrderById(id);

            order.calculateTotal();
            order.markAsProcessed();
            orderRepository.save(order);

            orderEventPublisher.sendProcessedOrder(order);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid UUID format received: {}", orderId);
        } catch (Exception e) {
            handleProcessingError(orderId, e);
        }
    }

    private Order fetchOrderById(UUID id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> {
                LOGGER.warn("Order with ID {} not found in the database.", id);
                return new IllegalArgumentException("Order not found: " + id);
            });
    }

    private void handleProcessingError(String orderId, Exception e) {
        LOGGER.error("Error processing order {}: {}", orderId, e.getMessage(), e);

        try {
            Order order = fetchOrderById(UUID.fromString(orderId));
            order.markAsError();
            orderRepository.save(order);
        } catch (Exception ex) {
            LOGGER.error("Failed to update order {} status to ERROR: {}", orderId, ex.getMessage(), ex);
        }
    }
}
