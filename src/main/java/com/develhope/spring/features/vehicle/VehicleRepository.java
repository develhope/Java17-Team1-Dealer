package com.develhope.spring.features.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
    @Query
    List<VehicleEntity> findByVehicleStatusAndUsed(VehicleStatus status, Boolean used);
}
