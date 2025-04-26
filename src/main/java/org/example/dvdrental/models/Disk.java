package org.example.dvdrental.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "disks")
@Getter
@Setter
@NoArgsConstructor
public class Disk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean available;
    @Column(name = "price_per_day", precision = 6, scale = 2)
    private BigDecimal pricePerDay;
}
