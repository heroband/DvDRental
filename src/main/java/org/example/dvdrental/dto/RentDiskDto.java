package org.example.dvdrental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentDiskDto {
    @Email(message = "Email must be valid")
    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Disk ID is required")
    private Long diskId;

    @NotNull(message = "Number of rental days is required")
    @Min(value = 1, message = "You must rent for at least 1 day")
    private Integer days;
}
