package org.example.dvdrental.controllers;

import jakarta.validation.Valid;
import org.example.dvdrental.dto.ApiResponse;
import org.example.dvdrental.dto.RentDiskDto;
import org.example.dvdrental.dto.ReturnDiskDto;
import org.example.dvdrental.models.UserRental;
import org.example.dvdrental.services.DiskService;
import org.example.dvdrental.services.UserRentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rentals")
public class RentalController {
    private final UserRentalService userRentalService;

    public RentalController(UserRentalService userRentalService) {
        this.userRentalService = userRentalService;
    }

    @GetMapping("/rent")
    public String showRentForm(@RequestParam(required = false) Long diskId, Model model) {
        RentDiskDto rentDiskDto = new RentDiskDto();
        if (diskId != null) {
            rentDiskDto.setDiskId(diskId);
        }
        model.addAttribute("rentDiskDto", rentDiskDto);
        return "rent-form";
    }

    @PostMapping("/rent")
    public String rentDisk(@ModelAttribute("rentDiskDto") @Valid RentDiskDto rentDiskDto,
                           BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors()) {
            return "rent-form";
        }

        try {
            var rental = userRentalService.rentDisk(rentDiskDto);
            model.addAttribute("message", "Disk rented successfully");
            model.addAttribute("rental", rental);
            return "rental-success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "rent-form";
        }
    }

    @GetMapping("/return")
    public String showReturnForm(@RequestParam(required = false) Long diskId, Model model) {
        ReturnDiskDto returnDiskDto = new ReturnDiskDto();
        model.addAttribute("returnDiskDto", returnDiskDto);
        model.addAttribute("unavailableDisks", userRentalService.getAllUnavailableDisks());

        return "return-form";
    }

    @PostMapping("/return")
    public String returnDisk(@ModelAttribute("returnDiskDto") @Valid ReturnDiskDto returnDiskDto,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("unavailableDisks", userRentalService.getAllUnavailableDisks());
            return "return-form";
        }

        try {
            var rental = userRentalService.returnDisk(returnDiskDto.getDiskId(), returnDiskDto.getEmail());
            model.addAttribute("message", "Disk returned successfully");
            model.addAttribute("rental", rental);
            return "rental-success";
        } catch (RuntimeException e) {
            model.addAttribute("unavailableDisks", userRentalService.getAllUnavailableDisks());
            model.addAttribute("error", e.getMessage());
            return "return-form";
        }
    }
}
