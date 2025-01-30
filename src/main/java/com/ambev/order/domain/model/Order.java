package com.ambev.order.domain.model;

import com.ambev.order.domain.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String idExterno;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_processamento")
    private LocalDateTime dataProcessamento;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    protected Order(OrderBuilder builder) {
        this.idExterno = builder.idExterno;
        this.status = OrderStatus.PENDENTE;
        this.dataCriacao = LocalDateTime.now();
        this.items = builder.items;
    }

    public void calculateTotal() {
        this.valorTotal = items.stream()
            .map(item -> item.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void markAsProcessed() {
        this.status = OrderStatus.PROCESSADO;
        this.dataProcessamento = LocalDateTime.now();
    }

    public void markAsError() {
        this.status = OrderStatus.ERRO;
        this.dataProcessamento = LocalDateTime.now();
    }

}