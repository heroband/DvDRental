package org.example.dvdrental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnDiskDto {
    @NotNull
    private Long diskId;

    @NotNull
    @Email
    private String email;
}
