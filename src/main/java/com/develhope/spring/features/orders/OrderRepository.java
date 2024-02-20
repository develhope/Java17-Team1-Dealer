package com.develhope.spring.features.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query
    List<Order> findByOrderStatus(OrderStatus status);

    @Repository
    interface RentalRepository extends JpaRepository<Rental, Long> {
    }
}
