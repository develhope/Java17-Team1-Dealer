package com.develhope.spring.features.rentals;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface RentalRepository extends JpaRepository<RentalEntity, Long> {

    @Query(
            value = "SELECT * FROM order_entity o WHERE o.renter_id = ?1",
            nativeQuery = true)
    List<RentalEntity> findAllByRenterId(Long renterId);

    @Query(
            value = "SELECT * FROM order_entity o WHERE o.renter_id = ?1 and o.seller_id = ?2",
            nativeQuery = true)
    List<RentalEntity> findAllByRenterIdAndSellerId(Long renterId, Long sellerId);

    @Query(
            value = "SELECT * FROM order_entity o WHERE o.seller_id = ?1",
            nativeQuery = true)
    List<RentalEntity> findAllBySellerId(Long sellerId);


}