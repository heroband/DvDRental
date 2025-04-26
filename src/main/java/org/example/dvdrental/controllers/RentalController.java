package org.example.dvdrental.controllers;

import jakarta.validation.Valid;
import org.example.dvdrental.dto.RentDiskDto;
import org.example.dvdrental.models.UserRental;
import org.example.dvdrental.services.UserRentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rentals")
public class RentalController {
    private final UserRentalService userRentalService;

    public RentalController(UserRentalService userRentalService) {
        this.userRentalService = userRentalService;
    }

    @PostMapping("/rent")
    public ResponseEntity<UserRental> rentDisk(@RequestBody @Valid RentDiskDto rentDiskDto) {
        var rental = userRentalService.rentDisk(rentDiskDto);
        return ResponseEntity.ok(rental);
    }

    @PostMapping("/return/{diskId}")
    public ResponseEntity<String> returnDisk(@PathVariable Long diskId) {
        try {
            userRentalService.returnDisk(diskId);
            return ResponseEntity.ok("Disk returned successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
