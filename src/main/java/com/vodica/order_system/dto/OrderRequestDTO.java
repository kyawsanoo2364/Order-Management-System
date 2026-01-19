package com.vodica.order_system.dto;

import com.vodica.order_system.enums.OrderStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    @NotEmpty(message = "Order Items must contain at least 1 item")
    @Valid
    private List<OrderItemRequestDTO> orderItems;
}
