package com.develhope.spring.entities.operations;

import com.develhope.spring.entities.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Data startOfRental;
    private Data endOfRental;
    private Float dailyCostRental;
    private Float totalCostRental;
    private PaymentStatus status;
    private Vehicle vehicle;
}
