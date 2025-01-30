package com.ambev.order.application.usecase;

import com.ambev.order.application.dto.response.OrderResponse;
import com.ambev.order.application.service.OrderMapper;
import com.ambev.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListOrdersUseCase {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public ListOrdersUseCase(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderResponse> execute() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }
}