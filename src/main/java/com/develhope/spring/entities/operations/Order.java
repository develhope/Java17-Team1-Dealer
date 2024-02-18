package com.develhope.spring.entities.operations;

import com.develhope.spring.entities.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Float deposit;
    @Enumerated
    private PaymentStatus paymentStatus;
    @Enumerated
    private OrderStatus orderStatus;
    @OneToOne
    private Vehicle vehicle;
}
