package com.develhope.spring.features.users;

import io.micrometer.common.lang.NonNullFields;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@NonNullFields
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false)
    @NotBlank
    private String name;
    @Column(nullable = false)
    @NotBlank
    private String surname;
    @Column(unique = true)
    private String telephoneNumber;
    @Column(unique = true, nullable = false)
    @NotBlank
    private String email;
    @Column(nullable = false)
    @NotBlank
    private String password;
    @Enumerated(EnumType.STRING)
    private UserType userType;
}
