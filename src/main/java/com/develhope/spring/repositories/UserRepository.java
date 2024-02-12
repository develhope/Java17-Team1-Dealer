package com.develhope.spring.repositories;

import com.develhope.spring.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
