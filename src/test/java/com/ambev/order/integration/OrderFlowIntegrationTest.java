package com.ambev.order.integration;

import com.ambev.order.application.dto.request.OrderItemRequest;
import com.ambev.order.application.dto.request.OrderRequest;
import com.ambev.order.application.dto.response.OrderResponse;
import com.ambev.order.config.PostgresTestContainer;
import com.ambev.order.domain.enums.OrderStatus;
import com.ambev.order.domain.model.Order;
import com.ambev.order.domain.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = PostgresTestContainer.Initializer.class)
public class OrderFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    private UUID processedOrderId;

    @BeforeEach
    void setUp() {
        processedOrderId = null;
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }

    @Test
    void testOrderFlowEndToEnd() throws Exception {
        OrderRequest orderRequest = new OrderRequest(
            "123456",
            Collections.singletonList(new OrderItemRequest("Cerveja Brahma", 2, new BigDecimal("5.50")))
        );

        String requestJson = objectMapper.writeValueAsString(orderRequest);

        String responseJson = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", notNullValue()))
            .andReturn().getResponse().getContentAsString();

        OrderResponse orderResponse = objectMapper.readValue(responseJson, OrderResponse.class);
        UUID orderId = orderResponse.getId();

        Optional<Order> savedOrder = orderRepository.findById(orderId);
        assertThat(savedOrder).isPresent();
        assertThat(savedOrder.get().getStatus()).isEqualTo(OrderStatus.PENDENTE);

        await().atMost(Duration.ofSeconds(10)).until(() -> processedOrderId != null);

        Optional<Order> processedOrder = orderRepository.findById(processedOrderId);
        assertThat(processedOrder).isPresent();
        assertThat(processedOrder.get().getStatus()).isEqualTo(OrderStatus.PROCESSADO);
        assertThat(processedOrder.get().getValorTotal()).isEqualTo(new BigDecimal("11.00"));
    }

    @RabbitListener(queues = "processed-orders.queue")
    public void receiveProcessedOrder(Order order) {
        this.processedOrderId = order.getId();
    }
}
