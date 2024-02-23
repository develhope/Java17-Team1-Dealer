package com.develhope.spring.features.orders;

import com.develhope.spring.features.vehicle.VehicleEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private OffsetDateTime startOfRental;
    private OffsetDateTime endOfRental;
    private Float dailyCostRental;
    private Float totalCostRental;
    @Enumerated
    private PaymentStatus status;
    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private VehicleEntity vehicleEntity;
}
