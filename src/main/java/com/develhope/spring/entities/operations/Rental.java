package com.develhope.spring.entities.operations;

import com.develhope.spring.entities.vehicle.Vehicle;
import jakarta.persistence.*;
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
    private OffsetDateTime startOfRental;
    private OffsetDateTime endOfRental;
    private Float dailyCostRental;
    private Float totalCostRental;
    @Enumerated
    private PaymentStatus status;
    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
}
