package com.ambev.order.application.service;

import com.ambev.order.application.dto.request.OrderItemRequest;
import com.ambev.order.application.dto.request.OrderRequest;
import com.ambev.order.application.dto.response.OrderItemResponse;
import com.ambev.order.application.dto.response.OrderResponse;
import com.ambev.order.domain.model.Order;
import com.ambev.order.domain.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public Order toEntity(OrderRequest request, Supplier<Boolean> existsByIdExterno) {
        List<OrderItem> items = request.getItems().stream()
            .map(this::toEntity)
            .collect(Collectors.toList());

        return Order.builder()
            .idExterno(request.getIdExterno())
            .items(items)
            .validate(existsByIdExterno)
            .build();
    }

    public OrderResponse toResponse(Order order) {
        var items = order.getItems().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());

        return OrderResponse.builder()
            .id(order.getId())
            .idExterno(order.getIdExterno())
            .status(order.getStatus().getDescricao())
            .valorTotal(order.getValorTotal())
            .dataCriacao(order.getDataCriacao())
            .dataProcessamento(order.getDataProcessamento())
            .items(items)
            .build();
    }

    public OrderItemResponse toResponse(OrderItem item) {
        return OrderItemResponse.builder()
            .id(item.getId())
            .nome(item.getNome())
            .preco(item.getPreco())
            .quantidade(item.getQuantidade())
            .build();
    }

    private OrderItem toEntity(OrderItemRequest request) {
        return OrderItem.builder()
            .nome(request.getNome())
            .quantidade(request.getQuantidade())
            .preco(request.getPreco())
            .build();
    }
}