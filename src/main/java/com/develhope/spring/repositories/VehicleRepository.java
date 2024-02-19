package com.develhope.spring.repositories;

import com.develhope.spring.entities.vehicle.Vehicle;
import com.develhope.spring.entities.vehicle.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    @Query
    List<Vehicle> findByVehicleStatusAndUsed(VehicleStatus status, Boolean used);
}
