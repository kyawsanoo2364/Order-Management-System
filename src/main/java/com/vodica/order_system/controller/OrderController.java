package com.vodica.order_system.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.vodica.order_system.dto.OrderRequestDTO;
import com.vodica.order_system.dto.PageableDTO;
import com.vodica.order_system.dto.UpdateOrderStatusRequestDTO;
import com.vodica.order_system.reponse.ApiResponse;
import com.vodica.order_system.reponse.OrderResponse;
import com.vodica.order_system.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/my")
    public ResponseEntity<PageableDTO<OrderResponse>> getAllMyOrders(@PageableDefault(size = 10)Pageable pageable, Authentication authentication){
        PageableDTO<OrderResponse> orderResponsePageableDTO = orderService.getOrders(pageable, authentication.getName());
        return new ResponseEntity<>(orderResponsePageableDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id,Authentication authentication){
        OrderResponse res = orderService.getOrderById(id,authentication.getName());
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestBody UpdateOrderStatusRequestDTO requestDTO){
        OrderResponse response = orderService.updateOrderStatus(id,requestDTO.getStatus());
        return  new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequestDTO orderRequestDTO,Authentication authentication){
        OrderResponse response =  orderService.createOrder(orderRequestDTO,authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id,Authentication authentication){
        OrderResponse response = orderService.cancelOrder(id,authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<ApiResponse> payOrder(@PathVariable Long id,Authentication authentication) throws StripeException {
        PaymentIntent intent = orderService.createPaymentIntentForOrder(id,authentication.getName());
        return new ResponseEntity<>(
                new ApiResponse("success","Created order payment successfully",
                        Map.of("paymentIntentId",intent.getId())
                        ),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/stripe/webhook")
    public ResponseEntity<?> handleStripeWebhook(@RequestBody String payload,@RequestHeader("Stripe-Signature") String sigHeader) throws StripeException {
        orderService.handleStripeWebHook(payload,sigHeader);
        return ResponseEntity.ok("success");
    }

}
