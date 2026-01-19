package com.vodica.order_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity {
    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "stock",nullable = false)
    private Integer stock;

    @Column(name = "price",nullable = false)
    private BigDecimal price;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItem> orderItems;

    @Version
    @Column(nullable = false)
    private int version;
}
