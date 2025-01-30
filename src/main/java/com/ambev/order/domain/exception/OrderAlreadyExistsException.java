package com.ambev.order.domain.exception;

public class OrderAlreadyExistsException extends RuntimeException {

    public OrderAlreadyExistsException(String idExterno) {
        super("Já existe um pedido com o idExterno: " + idExterno);
    }
}