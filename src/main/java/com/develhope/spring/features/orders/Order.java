package com.develhope.spring.features.orders;

import com.develhope.spring.features.users.User;
import com.develhope.spring.features.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.*;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float deposit;
    @Enumerated
    private PaymentStatus paymentStatus;
    @Enumerated
    private OrderStatus orderStatus;
    @OneToOne
    private Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
