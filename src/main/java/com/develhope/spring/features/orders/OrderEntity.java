package com.develhope.spring.features.orders;

import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.vehicle.VehicleEntity;
import jakarta.persistence.*;
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
    private PaymentStatus paymentStatus;
    @Enumerated
    private OrderStatus orderStatus;
    @OneToOne
    private VehicleEntity vehicleEntity;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
