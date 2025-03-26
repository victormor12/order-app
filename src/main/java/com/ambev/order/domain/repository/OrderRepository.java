package com.ambev.order.domain.repository;

import com.ambev.order.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    boolean existsByIdExterno(String idExterno);
}
