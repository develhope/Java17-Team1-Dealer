package com.develhope.spring.features.rentals;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RentalRepository extends JpaRepository<RentalEntity, Long> {

}