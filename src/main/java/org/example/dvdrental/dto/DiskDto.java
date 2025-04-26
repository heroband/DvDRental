package org.example.dvdrental.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DiskDto {
    @NotBlank(message = "The disk name is required")
    private String name;

    @NotNull(message = "Price per day of rent is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "The price must be positive")
    private BigDecimal pricePerDay;
}
