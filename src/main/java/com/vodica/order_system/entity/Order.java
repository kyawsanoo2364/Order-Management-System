package com.vodica.order_system.entity;

import com.vodica.order_system.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "order")
    private List<OrderItem> orderItems;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    @Column(name = "total_amount",nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;
}
