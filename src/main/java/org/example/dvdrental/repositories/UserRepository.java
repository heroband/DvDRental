package org.example.dvdrental.repositories;

import org.example.dvdrental.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
