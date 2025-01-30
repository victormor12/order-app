package com.ambev.order.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Item de um pedido")
public class OrderItemRequest {

    @Schema(description = "Nome do item", example = "Cerveja Brahma")
    private String nome;

    @Schema(description = "Quantidade do item", example = "2")
    private Integer quantidade;

    @Schema(description = "Preço unitário do item", example = "5.50")
    private BigDecimal preco;
}