package com.vodica.order_system.repository;

import com.vodica.order_system.entity.Order;
import com.vodica.order_system.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndUser(Long orderId, User user);

    Page<Order> findAllByUser(Pageable pageable, User user);
}
