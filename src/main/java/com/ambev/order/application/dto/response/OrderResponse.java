package com.ambev.order.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "Resposta com os detalhes do pedido criado")
public class OrderResponse {

    @Schema(description = "Identificador do pedido", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Identificador externo do pedido", example = "123456")
    private String idExterno;

    @Schema(description = "Status do pedido", example = "PENDENTE")
    private String status;

    @Schema(description = "Valor total do pedido", example = "15.50")
    private BigDecimal valorTotal;

    @Schema(description = "Data de criação do pedido")
    private LocalDateTime dataCriacao;

    @Schema(description = "Data de processamento do pedido")
    private LocalDateTime dataProcessamento;

    @Schema(description = "Lista de itens do pedido")
    private List<OrderItemResponse> items;
}