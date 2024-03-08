package com.develhope.spring.features.orders;

import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.vehicle.VehicleEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Table(name = "orders")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false, updatable = false)
    private Long id;
    private Long deposit;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotBlank
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotBlank
    private OrderStatus orderStatus;
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private OffsetDateTime orderDate;
    @Column(nullable = false, updatable = false)
    private Long orderPrice;
    @ManyToOne
    @NotBlank
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleEntity vehicleEntity;
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private UserEntity buyer;
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private UserEntity seller;
}
