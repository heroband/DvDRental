package org.example.dvdrental.repositories;

import org.example.dvdrental.models.UserRental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRentalRepository extends JpaRepository<UserRental, Long> {
}
