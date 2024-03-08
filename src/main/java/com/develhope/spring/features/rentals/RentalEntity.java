package com.develhope.spring.features.rentals;

import com.develhope.spring.features.orders.PaymentStatus;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.vehicle.VehicleEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Table(name = "rentals")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalEntity {
    @Id
    @Column(name = "rental_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private OffsetDateTime startOfRental;
    private OffsetDateTime endOfRental;
    private Long dailyCostRental;
    private Long totalCostRental;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotBlank
    private PaymentStatus paymentStatus;
    @ManyToOne
    @NotBlank
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleEntity vehicleEntity;
    @ManyToOne
    @JoinColumn(name = "renter_id")
    private UserEntity renter;
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private UserEntity seller;
}
