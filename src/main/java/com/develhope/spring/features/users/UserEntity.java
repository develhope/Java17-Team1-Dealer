package com.develhope.spring.features.users;

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


    /*@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UserEntity user = (UserEntity) obj;
        return id.equals(user.id) || email.equals(user.email) || (name.equals(user.name) && surname.equals(user.surname)) || surname.equals(user.surname) || telephoneNumber.equals(user.telephoneNumber);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }*/
}
