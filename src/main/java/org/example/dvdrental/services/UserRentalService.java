package org.example.dvdrental.services;

import jakarta.transaction.Transactional;
import org.example.dvdrental.dto.RentDiskDto;
import org.example.dvdrental.models.Disk;
import org.example.dvdrental.models.User;
import org.example.dvdrental.models.UserRental;
import org.example.dvdrental.repositories.DiskRepository;
import org.example.dvdrental.repositories.UserRentalRepository;
import org.example.dvdrental.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserRentalService {
    private final UserRentalRepository userRentalRepository;
    private final UserRepository userRepository;
    private final DiskRepository diskRepository;

    public UserRentalService(UserRentalRepository userRentalRepository, UserRepository userRepository, DiskRepository diskRepository) {
        this.userRentalRepository = userRentalRepository;
        this.userRepository = userRepository;
        this.diskRepository = diskRepository;
    }

    @Transactional
    public UserRental rentDisk(RentDiskDto rentDiskDto) {
        Disk disk = diskRepository.findById(rentDiskDto.getDiskId())
                .orElseThrow(()->new RuntimeException("Disk not found"));

        if (!disk.isAvailable()) {
            throw new RuntimeException("Disk is not available for rent");
        }

        User user = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(rentDiskDto.getEmail()))
                .findFirst()
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(rentDiskDto.getEmail());
                    return userRepository.save(newUser);
                });

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(rentDiskDto.getDays());

        UserRental rental = new UserRental();
        rental.setUser(user);
        rental.setDisk(disk);
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setTotalPrice(disk.getPricePerDay().multiply(
                BigDecimal.valueOf(rentDiskDto.getDays())
        ));

        disk.setAvailable(false);
        diskRepository.save(disk);

        return userRentalRepository.save(rental);
    }

    public List<UserRental> getAllRentals() {
        return userRentalRepository.findAll();
    }

    public UserRental getRentalById(Long id) {
        return userRentalRepository.findById(id).orElse(null);
    }

    @Transactional
    public UserRental returnDisk(Long diskId){
        UserRental rental = userRentalRepository.findAll().stream()
                .filter(r -> r.getDisk().getId().equals(diskId))
                .findFirst()
                .orElseThrow(()->new RuntimeException("Rental not found for this disk"));

        Disk disk = rental.getDisk();
        disk.setAvailable(true);
        diskRepository.save(disk);

        userRentalRepository.delete(rental);
        return rental;
    }
}
