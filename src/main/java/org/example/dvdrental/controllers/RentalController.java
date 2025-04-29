package org.example.dvdrental.controllers;

import jakarta.validation.Valid;
import org.example.dvdrental.dto.ApiResponse;
import org.example.dvdrental.dto.RentDiskDto;
import org.example.dvdrental.dto.ReturnDiskDto;
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
    public ResponseEntity<ApiResponse<UserRental>> rentDisk(@RequestBody @Valid RentDiskDto rentDiskDto) {
        try {
            var rental = userRentalService.rentDisk(rentDiskDto);
            return ResponseEntity.ok(new ApiResponse<>("Disk rented successfully", rental));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @PostMapping("/return")
    public ResponseEntity<ApiResponse<UserRental>> returnDisk(@RequestBody @Valid ReturnDiskDto returnDiskDto) {
        try {
            var rental = userRentalService.returnDisk(returnDiskDto.getDiskId(), returnDiskDto.getEmail());
            return ResponseEntity.ok(new ApiResponse<>("Disk returned successfully", rental));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        }
    }
}
