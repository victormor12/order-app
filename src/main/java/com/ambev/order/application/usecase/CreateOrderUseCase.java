package com.ambev.order.application.usecase;

import com.ambev.order.application.dto.request.OrderRequest;
import com.ambev.order.application.dto.response.OrderResponse;
import com.ambev.order.application.service.OrderMapper;
import com.ambev.order.domain.repository.OrderRepository;
import com.ambev.order.infrastructure.messaging.OrderEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderEventPublisher orderEventPublisher;

    public CreateOrderUseCase(OrderRepository orderRepository,
                              OrderMapper orderMapper, OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderEventPublisher = orderEventPublisher;
    }

    public OrderResponse execute(OrderRequest request) {
        var order = orderMapper.toEntity(request, () -> orderRepository.existsByIdExterno(request.getIdExterno()));
        var savedOrder = orderRepository.saveAndFlush(order);

        orderEventPublisher.sendOrderToQueue(savedOrder.getId());

        return orderMapper.toResponse(savedOrder);
    }

}
