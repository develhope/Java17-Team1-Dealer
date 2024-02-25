package com.develhope.spring.features.orders;

import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.vehicle.VehicleEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float deposit;
    @Enumerated
    @Column(nullable = false)
    @NotBlank
    private PaymentStatus paymentStatus;
    @Enumerated
    @Column(nullable = false)
    @NotBlank
    private OrderStatus orderStatus;
    @OneToOne
    @NotBlank
    private VehicleEntity vehicleEntity;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotBlank
    private UserEntity userEntity;
}
