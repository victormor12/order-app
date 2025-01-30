package com.ambev.order.application.usecase;

import com.ambev.order.application.dto.response.OrderResponse;
import com.ambev.order.application.service.OrderMapper;
import com.ambev.order.domain.model.Order;
import com.ambev.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetOrderByIdUseCase {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public GetOrderByIdUseCase(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public OrderResponse execute(UUID id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        return orderMapper.toResponse(order);
    }
}