package com.develhope.spring.features.rentals;

import com.develhope.spring.features.orders.PaymentStatus;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.vehicle.VehicleEntity;
import io.micrometer.common.lang.NonNullFields;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.OffsetDateTime;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNullFields
public class RentalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private OffsetDateTime startOfRental;
    private OffsetDateTime endOfRental;
    private Integer dailyCostRental;
    private Integer totalCostRental;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotBlank
    private PaymentStatus paymentStatus;
    @OneToOne
    @NotBlank
    @JoinColumn(name = "vehicle", nullable = false)
    private VehicleEntity vehicleEntity;
    @ManyToOne
    @JoinColumn(name = "renter", nullable = false)
    @NotBlank
    private UserEntity renter;
}
