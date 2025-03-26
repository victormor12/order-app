package com.ambev.order.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTest {

    @Test
    void shouldCalculateTotalCorrectly() {
        OrderItem item1 = OrderItem.builder()
            .id(UUID.randomUUID())
            .nome("Cerveja")
            .quantidade(2).preco(new BigDecimal("5.00"))
            .build();

        OrderItem item2 = OrderItem.builder()
            .id(UUID.randomUUID())
            .nome("Refrigerante")
            .quantidade(3)
            .preco(new BigDecimal("3.50"))
            .build();

        Order order = Order.builder()
            .idExterno("1234")
            .items(Arrays.asList(item1, item2))
            .build();

        order.calculateTotal();

        assertEquals(new BigDecimal("20.50"), order.getValorTotal());
    }
}
