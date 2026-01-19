package com.vodica.order_system.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vodica.order_system.dto.OrderItemDTO;
import com.vodica.order_system.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    private Long id;
    private OrderStatusEnum status;
    private BigDecimal totalAmount;
    private List<OrderItemDTO> orderItems;
    private String paymentIntentId;
    private List<OrderStatusEnum> nextStatues = null;
}
