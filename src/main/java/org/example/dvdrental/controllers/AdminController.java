package org.example.dvdrental.controllers;

import org.example.dvdrental.dto.ApiResponse;
import org.example.dvdrental.models.User;
import org.example.dvdrental.models.UserRental;
import org.example.dvdrental.services.UserRentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserRentalService userRentalService;

    public AdminController(UserRentalService userRentalService) {
        this.userRentalService = userRentalService;
    }

    @GetMapping("/rentals")
    public ResponseEntity<ApiResponse<List<UserRental>>> getAllRentals() {
        List<UserRental> rentals = userRentalService.getAllRentals();
        return ResponseEntity.ok(new ApiResponse<>("All rentals fetched successfully", rentals));
    }
}
