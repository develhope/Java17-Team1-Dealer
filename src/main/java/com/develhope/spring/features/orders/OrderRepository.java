package com.develhope.spring.features.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query
    List<OrderEntity> findByOrderStatus(OrderStatus status);

    @Query(
            value = "SELECT * FROM order_entity o WHERE o.buyer = ?1",
            nativeQuery = true)
    List<OrderEntity> findAllByBuyer(Long buyerId);

    @Query(
        value = "SELECT * FROM order_entity o WHERE o.buyer = ?1 and o.payment_status = 'PAID'",
        nativeQuery = true)
    List<OrderEntity> findAllByBuyerPaymentStatusIsPaid(Long buyerId);
}
