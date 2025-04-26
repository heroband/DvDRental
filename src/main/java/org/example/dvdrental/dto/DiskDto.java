package org.example.dvdrental.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DiskDto {
    private String name;
    private BigDecimal pricePerDay;
}
