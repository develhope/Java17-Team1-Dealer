package com.develhope.spring.features.vehicle;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long>, JpaSpecificationExecutor<VehicleEntity> {
    @Query
    List<VehicleEntity> findByVehicleStatus(VehicleStatus status);

    List<VehicleEntity> findAll(Specification<VehicleEntity> specification);
}
