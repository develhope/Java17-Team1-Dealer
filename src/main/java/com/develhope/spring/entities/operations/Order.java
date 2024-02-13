package com.develhope.spring.entities.operations;

import com.develhope.spring.entities.users.User;
import com.develhope.spring.entities.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
