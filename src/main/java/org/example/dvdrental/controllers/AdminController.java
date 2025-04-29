package org.example.dvdrental.controllers;

import org.example.dvdrental.models.UserRental;
import org.example.dvdrental.services.UserRentalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRentalService userRentalService;

    public AdminController(UserRentalService userRentalService) {
        this.userRentalService = userRentalService;
    }

    @GetMapping
    public String showAllRentals(Model model) {
        List<UserRental> rentals = userRentalService.getAllRentals();
        model.addAttribute("rentals", rentals);
        return "admin-rentals";
    }
}