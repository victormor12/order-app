package com.ambev.order.infrastructure.controller;

import com.ambev.order.application.dto.request.OrderRequest;
import com.ambev.order.application.dto.response.OrderResponse;
import com.ambev.order.application.usecase.CreateOrderUseCase;
import com.ambev.order.application.usecase.GetOrderByIdUseCase;
import com.ambev.order.application.usecase.ListOrdersUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Tag(name = "Pedidos", description = "API para gerenciamento de pedidos")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final ListOrdersUseCase listOrdersUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase,
                           GetOrderByIdUseCase getOrderByIdUseCase,
                           ListOrdersUseCase listOrdersUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderByIdUseCase = getOrderByIdUseCase;
        this.listOrdersUseCase = listOrdersUseCase;
    }

    @PostMapping
    @Operation(summary = "Criar um novo pedido", description = "Cria um pedido e retorna os detalhes do pedido criado.")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        OrderResponse orderResponse = createOrderUseCase.execute(request);
        URI location = URI.create("/orders/" + orderResponse.getId());
        return ResponseEntity.created(location).body(orderResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna os detalhes de um pedido pelo seu ID.")
    public ResponseEntity<OrderResponse> getOrderById(
        @Parameter(description = "ID do pedido", required = true)
        @PathVariable UUID id) {
        return ResponseEntity.ok(getOrderByIdUseCase.execute(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos os pedidos", description = "Retorna uma lista com todos os pedidos cadastrados.")
    public ResponseEntity<List<OrderResponse>> listOrders() {
        return ResponseEntity.ok(listOrdersUseCase.execute());
    }
}