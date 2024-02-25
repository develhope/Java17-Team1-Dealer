package com.develhope.spring.features.rentals;

import com.develhope.spring.features.orders.PaymentStatus;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.vehicle.VehicleEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @CreationTimestamp
    private OffsetDateTime startOfRental;
    private OffsetDateTime endOfRental;
    private Float dailyCostRental;
    private Float totalCostRental;
    @Enumerated
    @Column(nullable = false)
    @NotBlank
    private PaymentStatus status;
    @OneToOne
    @NotBlank
    @JoinColumn(nullable = false)
    private VehicleEntity vehicleEntity;
    @ManyToOne
    @JoinColumn(nullable = false)
    @NotBlank
    private UserEntity userEntity;
}
