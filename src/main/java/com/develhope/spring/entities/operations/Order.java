package com.develhope.spring.entities.operations;

import com.develhope.spring.entities.users.User;
import com.develhope.spring.entities.vehicle.Vehicle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    private Long id;
    private Float deposit;
    @Enumerated
    @NotBlank
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.NOT_PAID;
    @Enumerated
    private OrderStatus orderStatus = OrderStatus.TO_SEND;
    @OneToOne
    @NotBlank
    @Column(nullable = false)
    private Vehicle vehicle;
    @ManyToOne
    @NotBlank
    @Column(nullable = false)
    @JoinColumn(name = "user_id")
    private User user;
}
