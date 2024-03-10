package com.develhope.spring.features.orders;

import com.develhope.spring.features.vehicle.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query
    List<OrderEntity> findByOrderStatus(OrderStatus status);

    @Query(
            value = "SELECT * FROM orders o WHERE o.order_status = ?1 and o.buyer_id = ?2",
            nativeQuery = true)
    List<OrderEntity> findByOrderStatusBuyer(OrderStatus status, Long userId);

    @Query(
            value = "SELECT * FROM orders o WHERE o.order_status = ?1 and and o.buyer_id = ?2 and o.seller_id = ?3",
            nativeQuery = true)
    List<OrderEntity> findByOrderStatusSeller(OrderStatus status, Long sellerId);

    @Query(
            value = "SELECT * FROM orders o WHERE o.order_status = ?1 and and o.buyer_id = ?2 and o.seller_id = ?3",
            nativeQuery = true)
    List<OrderEntity> findByOrderStatusSellerAndBuyer(OrderStatus status, Long buyerId, Long sellerId);

    @Query(
            value = "SELECT * FROM orders o WHERE o.buyer_id = ?1",
            nativeQuery = true)
    List<OrderEntity> findAllByBuyerId(Long buyerId);

    @Query(
            value = "SELECT * FROM orders o WHERE o.seller_id = ?1",
            nativeQuery = true)
    List<OrderEntity> findAllBySellerId(Long sellerId);

    @Query(
            value = "SELECT * FROM orders o WHERE o.seller_id = ?1 and o.buyer_id = ?2",
            nativeQuery = true)
    List<OrderEntity> findAllByBuyerIdAndSellerId(Long sellerId, Long buyerId);

    @Query(
            value = "SELECT * FROM orders o WHERE o.buyer_id = ?1 and o.payment_status = 'PAID'",
            nativeQuery = true)
    List<OrderEntity> findAllByBuyerPaymentStatusIsPaid(Long buyerId);

    @Query(
            value = "SELECT * FROM orders o WHERE o.seller_id = ?1 and o.payment_status = 'PAID'",
            nativeQuery = true)
    List<OrderEntity> findAllBySellerPaymentStatusIsPaid(Long sellerId);

    @Query(
            value = "SELECT * FROM orders o WHERE o.buyer_id = ?1 and o.seller_id = ?2 and o.payment_status = 'PAID'",
            nativeQuery = true)
    List<OrderEntity> findAllByBuyerAndSellerPaymentStatusIsPaid(Long buyerId, Long sellerId);

    @Query(
            value = "SELECT vehicle_id FROM orders o WHERE (o.order_date BETWEEN ?1 AND ?2) AND o.payment_status = 'PAID' GROUP BY vehicle_id ORDER BY COUNT(*) DESC LIMIT 1",
            nativeQuery = true)
    VehicleEntity findMostSoldInAPeriod(OffsetDateTime startDate, OffsetDateTime endDate);

    @Query(
            value = "SELECT vehicle_id FROM orders o WHERE (o.order_date BETWEEN ?1 AND ?2) AND (o.payment_status != 'NOT_PAID' && o.payment_status != 'PENDING') GROUP BY vehicle_id ORDER BY COUNT(*) DESC LIMIT 1",
            nativeQuery = true)
    VehicleEntity findMostOrderedInAPeriod(OffsetDateTime startDate, OffsetDateTime endDate);

    @Query(
            value = "SELECT vehicle_id FROM orders o ORDER BY o.order_price DESC LIMIT 1",
            nativeQuery = true)
    VehicleEntity findHighestPriceSold();

    @Query(
            value = "SELECT vehicle_id FROM orders o ORDER BY o.order_price ASC LIMIT 1",
            nativeQuery = true)
    VehicleEntity findLowestPriceSold();

    @Query(
            value = "SELECT COUNT(*) FROM orders o WHERE o.seller_id = ?1",
            nativeQuery = true)
    Long getSalesCountBySellerId(Long seller_id);

    @Query(
            value = "SELECT SUM(o.price) FROM orders o WHERE o.seller_id = ?1",
            nativeQuery = true)
    Long getSalesTotalPriceBySellerId(Long sellerId);

    @Query(
            value = "SELECT SUM(o.order_price) FROM orders o WHERE o.order_date BETWEEN ?1 AND ?2",
            nativeQuery = true)
    Long getTotalSalesPriceInAPeriod(OffsetDateTime startDate, OffsetDateTime endDate);
}
