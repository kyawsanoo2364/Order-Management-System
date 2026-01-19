package com.vodica.order_system.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import com.vodica.order_system.dto.OrderRequestDTO;
import com.vodica.order_system.dto.PageableDTO;
import com.vodica.order_system.entity.Order;
import com.vodica.order_system.entity.OrderItem;
import com.vodica.order_system.entity.Product;
import com.vodica.order_system.entity.User;
import com.vodica.order_system.enums.OrderStatusEnum;
import com.vodica.order_system.exceptions.BusinessException;
import com.vodica.order_system.exceptions.ResourceNotFoundException;
import com.vodica.order_system.reponse.OrderResponse;
import com.vodica.order_system.repository.OrderRepository;
import com.vodica.order_system.repository.ProductRepository;
import com.vodica.order_system.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;


    @Transactional
    public OrderResponse createOrder(OrderRequestDTO orderRequestDTO,String username) {
        //first find logged-in user
        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        //create empty order entity first
        Order order = Order.builder()
                .status(OrderStatusEnum.CREATED)
                .user(user)
                .totalAmount(BigDecimal.ZERO)
                .build();
        List<OrderItem> orderItems = new ArrayList<>();
        orderRequestDTO.getOrderItems().forEach((orderItem)->{
            Product product = productRepository.findById(orderItem.getProductId()).orElseThrow(()->
                    new ResourceNotFoundException("Invalid Product")
                    );
            //check if stock is enough
            if(product.getStock() < orderItem.getQuantity()){
                throw new BusinessException("Not enough stock");
            }
            //if enough stock - quantity
            product.setStock(product.getStock()-orderItem.getQuantity());
            OrderItem item = OrderItem.builder()
                    .price(product.getPrice())
                    .product(product)
                    .order(order)
                    .quantity(orderItem.getQuantity())
                    .subTotal(product.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                    .build();
            orderItems.add(item);
        });
        BigDecimal totalAmount = orderItems.stream().map(OrderItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        //create order
        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder,OrderResponse.class);
    }

    public OrderResponse getOrderById(Long orderId,String username){
        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Order order = orderRepository.findByIdAndUser(orderId,user).orElseThrow(()->new ResourceNotFoundException("Order not found"));
        OrderResponse orderResponse = modelMapper.map(order,OrderResponse.class);
        orderResponse.setNextStatues(getNextOrderStatuses(order.getStatus()));
        return orderResponse;
    }

    public PageableDTO<OrderResponse> getOrders(Pageable pageable,String username){
        User user =  userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Page<OrderResponse> res = orderRepository.findAllByUser(pageable,user).map((order)->modelMapper.map(order,OrderResponse.class));
        return new PageableDTO<>(res);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId,OrderStatusEnum nextStatus){
        Order existsOrder = orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order not found"));

        // 🔒 block modification of final state
        if(existsOrder.getStatus() == OrderStatusEnum.COMPLETED){
            throw new BusinessException("Completed order cannot be modified");
        }

        // 🚦 validate transition
        if(!getNextOrderStatuses(existsOrder.getStatus()).contains(nextStatus)){
            throw new BusinessException("Invalid status transition from "+ existsOrder.getStatus() +" to "+nextStatus);
        }
        existsOrder.setStatus(nextStatus);
        orderRepository.save(existsOrder);
        return modelMapper.map(existsOrder,OrderResponse.class);
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId,String username){
        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Order existsOrder = orderRepository.findByIdAndUser(orderId,user).orElseThrow(()->new ResourceNotFoundException("Order not found"));
        if(!OrderStatusEnum.CREATED.equals(existsOrder.getStatus())){
            throw new BusinessException("Only CREATED orders can be cancelled");
        }

        existsOrder.setStatus(OrderStatusEnum.CANCELLED);
        existsOrder.getOrderItems().forEach((orderItem)->{
            Product product = orderItem.getProduct();
            product.setStock(product.getStock() +  orderItem.getQuantity());
            productRepository.save(product);
        });
       var savedOrder = orderRepository.save(existsOrder);
        return  modelMapper.map(savedOrder,OrderResponse.class);
    }

    @Transactional
    public PaymentIntent createPaymentIntentForOrder(Long orderId,String username) throws StripeException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Order order = orderRepository.findByIdAndUser(orderId,user).orElseThrow(()->new ResourceNotFoundException("Order not found"));
       if(!OrderStatusEnum.CREATED.equals(order.getStatus())){
           throw new BusinessException("Only CREATED orders can be payment intent");
       }
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(order.getTotalAmount().longValue())
                .setCurrency("usd")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .setAllowRedirects(
                                        PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER
                                )
                                .build()
                )
                .putMetadata("orderId",order.getId().toString())
                .build();
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        order.setPaymentIntentId(paymentIntent.getId());
        orderRepository.save(order);

        return paymentIntent;
    }

    @Transactional
    public void handleStripeWebHook(String payload,String sigHeader) throws StripeException {
        Event event = Webhook.constructEvent(
                payload,
                sigHeader,
                webhookSecret
        );
        log.info("Check "+event.getType());
        if("payment_intent.succeeded".equals(event.getType())){

            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
            if(deserializer.getObject().isEmpty()){
                log.warn("Stripe event data object is empty, type={}", event.getType());
                return;
            }
            PaymentIntent intent = (PaymentIntent) deserializer.getObject().get();


            Long orderId = Long.valueOf(intent.getMetadata().get("orderId"));

            Order order = orderRepository.findById(orderId).orElse(null);
            if(order == null) {
                log.warn("Order not found with id " + orderId);
                return;
            };
            if(!OrderStatusEnum.CREATED.equals(order.getStatus())){
                log.warn("Order status is not equal CREATED");
                return;
            }

            if(intent.getId().equals(order.getPaymentIntentId())){

                order.setStatus(OrderStatusEnum.PAID);
                orderRepository.save(order);
            }

        }

    }


    private List<OrderStatusEnum> getNextOrderStatuses(OrderStatusEnum current){
        if(current == null){
            return List.of();
        }
        switch (current){
            case CREATED:
                return List.of(OrderStatusEnum.PAID,OrderStatusEnum.CANCELLED);
                case PAID:
                    return List.of(OrderStatusEnum.SHIPPED);
                    case SHIPPED:
                        return List.of(OrderStatusEnum.DELIVERED);
            case DELIVERED:
                return List.of(OrderStatusEnum.COMPLETED);
            default:
                return List.of();
        }
    }
}
