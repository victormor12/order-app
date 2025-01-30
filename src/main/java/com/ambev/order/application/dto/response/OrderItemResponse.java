package com.ambev.order.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "Detalhes do item do pedido")
public class OrderItemResponse {

    @Schema(description = "Identificador do item", example = "aa5f3dcd-8f9a-4f4a-b564-3f4b90b2a74a")
    private UUID id;

    @Schema(description = "Nome do item", example = "Cerveja Brahma")
    private String nome;

    @Schema(description = "Quantidade do item", example = "2")
    private Integer quantidade;

    @Schema(description = "Preço unitário do item", example = "5.50")
    private BigDecimal preco;
}