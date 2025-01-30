package com.ambev.order.domain.model;

import com.ambev.order.domain.exception.OrderAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class OrderBuilder {
    protected String idExterno;
    protected List<OrderItem> items = new ArrayList<>();
    protected Supplier<Boolean> existsByIdExterno;

    public OrderBuilder idExterno(String idExterno) {
        this.idExterno = idExterno;
        return this;
    }

    public OrderBuilder items(List<OrderItem> items) {
        this.items = (items != null) ? items : new ArrayList<>();
        return this;
    }

    public OrderBuilder validate(Supplier<Boolean> existsByIdExterno) {
        this.existsByIdExterno = existsByIdExterno;
        return this;
    }

    public Order build() {
        if (existsByIdExterno != null && existsByIdExterno.get()) {
            throw new OrderAlreadyExistsException(idExterno);
        }
        return new Order(this);
    }
}