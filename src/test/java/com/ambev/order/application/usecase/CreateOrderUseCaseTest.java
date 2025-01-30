package com.ambev.order.application.usecase;

import com.ambev.order.application.dto.request.OrderRequest;
import com.ambev.order.application.dto.response.OrderResponse;
import com.ambev.order.application.service.OrderMapper;
import com.ambev.order.domain.model.Order;
import com.ambev.order.domain.repository.OrderRepository;
import com.ambev.order.infrastructure.messaging.OrderEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderEventPublisher orderEventPublisher;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    private OrderRequest orderRequest;
    private Order order;
    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        orderRequest = new OrderRequest();
        orderRequest.setIdExterno("1234");

        order = Order.builder()
            .idExterno("1234")
            .items(Collections.emptyList())
            .build();

        orderResponse = OrderResponse.builder()
            .id(UUID.randomUUID())
            .idExterno("1234")
            .valorTotal(BigDecimal.ZERO)
            .build();
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        when(orderMapper.toEntity(any(), any())).thenReturn(order);
        when(orderRepository.existsByIdExterno(orderRequest.getIdExterno())).thenReturn(false);
        when(orderRepository.saveAndFlush(order)).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);

        OrderResponse response = createOrderUseCase.execute(orderRequest);

        assertNotNull(response);
        assertEquals(orderRequest.getIdExterno(), response.getIdExterno());
        verify(orderEventPublisher).sendOrderToQueue(order.getId());
    }
}
