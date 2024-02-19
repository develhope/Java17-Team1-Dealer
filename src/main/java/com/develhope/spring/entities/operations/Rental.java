package com.develhope.spring.entities.operations;

import com.develhope.spring.entities.vehicle.Vehicle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
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
    @NotBlank
    @Column(nullable = false)
    private OffsetDateTime startOfRental;
    @NotBlank
    @Column(nullable = false)
    private OffsetDateTime endOfRental;
    @NotBlank
    @Column(nullable = false)
    private Float dailyCostRental;
    @NotBlank
    @Column(nullable = false)
    private Float totalCostRental;
    @Enumerated
    @NotBlank
    @Column(nullable = false)
    private PaymentStatus status;
    @OneToOne
    @NotBlank
    @Column(nullable = false)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
}
