package com.vodica.order_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long productId;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subTotal;
}
