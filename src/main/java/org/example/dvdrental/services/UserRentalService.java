package org.example.dvdrental.services;

import org.example.dvdrental.models.UserRental;
import org.example.dvdrental.repositories.UserRentalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRentalService {
    private final UserRentalRepository userRentalRepository;

    public UserRentalService(UserRentalRepository userRentalRepository) {
        this.userRentalRepository = userRentalRepository;
    }

    public UserRental createRental(UserRental rental) {
        return userRentalRepository.save(rental);
    }

    public List<UserRental> getAllRentals() {
        return userRentalRepository.findAll();
    }

    public UserRental getRentalById(Long id) {
        return userRentalRepository.findById(id).orElse(null);
    }

    public UserRental deleteRentalById(Long id) {
        UserRental rental = userRentalRepository.findById(id).orElse(null);
        if (rental != null) {
            userRentalRepository.delete(rental);
        }
        return rental;
    }
}
