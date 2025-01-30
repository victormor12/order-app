package com.ambev.order.domain.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDENTE("Pendente"),
    PROCESSADO("Processado"),
    ERRO("Erro");

    private final String descricao;

    OrderStatus(String descricao) {
        this.descricao = descricao;
    }
}