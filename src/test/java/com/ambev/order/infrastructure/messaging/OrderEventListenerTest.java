package com.ambev.order.infrastructure.messaging;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ambev.order.domain.model.Order;
import com.ambev.order.domain.model.OrderItem;
import com.ambev.order.domain.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class OrderEventListenerTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventPublisher orderEventPublisher;

    @InjectMocks
    private OrderEventListener orderEventListener;

    private UUID orderId;
    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        orderId = UUID.randomUUID();

        OrderItem orderItem = OrderItem.builder()
            .id(UUID.randomUUID())
            .nome("Skol")
            .preco(BigDecimal.TEN)
            .quantidade(1)
            .build();

        order = Order.builder()
            .idExterno("1234")
            .items(List.of(orderItem))
            .build();
    }

    @Test
    void shouldProcessOrderSuccessfully() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderEventListener.processOrder(orderId.toString());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order processedOrder = orderCaptor.getValue();

        assertNotNull(processedOrder);
        assertEquals(order.getIdExterno(), processedOrder.getIdExterno());
        assertEquals(BigDecimal.TEN, processedOrder.getValorTotal());
        assertEquals("PROCESSADO", processedOrder.getStatus().name());

        verify(orderEventPublisher).sendProcessedOrder(processedOrder);
    }
}
