package com.develhope.spring.repositories;

import com.develhope.spring.entities.operations.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental,Long> {
}
