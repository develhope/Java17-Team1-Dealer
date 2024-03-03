package com.develhope.spring.features.rentals;

import com.develhope.spring.features.orders.OrderEntity;
import com.develhope.spring.features.vehicle.VehicleEntity;
import com.develhope.spring.features.vehicle.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface RentalRepository extends JpaRepository<RentalEntity, Long> {

    @Query(
            value = "SELECT * FROM order_entity o WHERE o.buyer = ?1",
            nativeQuery = true)
    List<RentalEntity> findAllByRenter(Long buyerId);


}