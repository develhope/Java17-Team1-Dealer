package com.develhope.spring.features.rentals;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface RentalRepository extends JpaRepository<RentalEntity, Long> {

    @Query(
            value = "SELECT * FROM order_entity o WHERE o.renter = ?1",
            nativeQuery = true)
    List<RentalEntity> findAllByRenter(Long renterId);


}