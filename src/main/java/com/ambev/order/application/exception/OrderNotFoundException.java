package com.ambev.order.application.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException() {
        super("O Pedido informado não existe em nossa base de dados.");
    }
}