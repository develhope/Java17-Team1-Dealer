package com.develhope.spring.features.orders;

import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.vehicle.VehicleEntity;
import io.micrometer.common.lang.NonNullFields;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNullFields
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer deposit;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotBlank
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotBlank
    private OrderStatus orderStatus;
    @OneToOne
    @NotBlank
    @JoinColumn(name = "vehicle", nullable = false)
    private VehicleEntity vehicleEntity;
    @ManyToOne
    @JoinColumn(name = "buyer", nullable = false)
    @NotBlank
    private UserEntity buyer;
}
