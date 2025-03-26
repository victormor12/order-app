package com.ambev.order.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Requisição para criar um novo pedido")
public class OrderRequest {

    @Schema(description = "Identificador externo do pedido", example = "123456")
    private String idExterno;

    @Schema(description = "Lista de itens do pedido")
    private List<OrderItemRequest> items;
}