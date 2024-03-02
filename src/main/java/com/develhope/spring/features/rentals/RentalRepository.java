package com.develhope.spring.features.rentals;

import com.develhope.spring.features.vehicle.VehicleEntity;
import com.develhope.spring.features.vehicle.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface RentalRepository extends JpaRepository<RentalEntity, Long> {

    @Query
    List<RentalEntity> findByUserID(Long UserID);

    @Query
    Optional<RentalEntity> findByUserIdAndRentalId(Long UserID, Long RentalID);






}