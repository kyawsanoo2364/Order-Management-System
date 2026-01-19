package com.vodica.order_system.dto;

import com.vodica.order_system.enums.OrderStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusRequestDTO {
    @NotNull
    private OrderStatusEnum status;
}
