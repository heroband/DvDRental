package org.example.dvdrental;

import org.example.dvdrental.dto.RentDiskDto;
import org.example.dvdrental.models.Disk;
import org.example.dvdrental.models.User;
import org.example.dvdrental.models.UserRental;
import org.example.dvdrental.repositories.DiskRepository;
import org.example.dvdrental.repositories.UserRentalRepository;
import org.example.dvdrental.repositories.UserRepository;
import org.example.dvdrental.services.UserRentalService;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserRentalServiceTest {
    @Mock
    private UserRentalRepository userRentalRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DiskRepository diskRepository;

    @InjectMocks
    private UserRentalService userRentalService;

    private RentDiskDto rentDiskDto;
    private Disk disk;

    @BeforeEach
    void setUp() {
        rentDiskDto = new RentDiskDto();
        rentDiskDto.setDiskId(1L);
        rentDiskDto.setEmail("test@example.com");
        rentDiskDto.setDays(5);

        disk = new Disk();
        disk.setId(1L);
        disk.setName("Test Disk");
        disk.setAvailable(true);
        disk.setPricePerDay(new BigDecimal("2.00"));
    }

    @Test
    void rentDisk_shouldCreateRental_whenDiskAvailableAndUserDoesNotExist(){
        when(diskRepository.findById(1L)).thenReturn(Optional.of(disk));

        // отримання юзерів, коли їх нема
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        User newUser = new User();
        newUser.setId(1L);
        newUser.setEmail("test@example.com");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // збереження оновленого диска
        when(diskRepository.save(any(Disk.class))).thenReturn(disk);

        // збереження оренди
        when(userRentalRepository.save(any(UserRental.class)))
                .thenAnswer(invocation -> {
                    UserRental rental = invocation.getArgument(0);
                    rental.setId(100L);
                    return rental;
                });

        UserRental result = userRentalService.rentDisk(rentDiskDto);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(newUser, result.getUser());
        assertEquals(disk, result.getDisk());
        assertEquals(LocalDate.now(), result.getStartDate());
        assertEquals(LocalDate.now().plusDays(5), result.getEndDate());
        assertEquals(new BigDecimal("10.00"), result.getTotalPrice());
        assertFalse(disk.isAvailable());

        verify(userRepository).save(any(User.class));
        verify(diskRepository).save(any(Disk.class));
        verify(userRentalRepository).save(any(UserRental.class));
    }

    @Test
    void rentDisk_shouldThrowException_whenDiskNotAvailable() {
        disk.setAvailable(false);
        when(diskRepository.findById(1L)).thenReturn(Optional.of(disk));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userRentalService.rentDisk(rentDiskDto);
        });

        assertEquals("Disk is not available for rent", exception.getMessage());
    }

    @Test
    void returnDisk_shouldSetDiskAvailableAndDeleteRental() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        when(userRepository.findAll()).thenReturn(List.of(user));

        UserRental rental = new UserRental();
        rental.setUser(user);
        disk.setAvailable(false);
        rental.setDisk(disk);

        when(userRentalRepository.findAll()).thenReturn(List.of(rental));
        when(diskRepository.save(any(Disk.class))).thenReturn(disk);

        UserRental result = userRentalService.returnDisk(disk.getId(), user.getEmail());

        assertTrue(result.getDisk().isAvailable()); // тепер доступний
        verify(diskRepository).save(any(Disk.class));
        verify(userRentalRepository).delete(rental);
    }

    @Test
    void returnDisk_shouldThrowException_whenUserNotFound() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userRentalService.returnDisk(1L, "unknown@example.com");
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void returnDisk_shouldThrowException_whenRentalNotFound() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        when(userRepository.findAll()).thenReturn(List.of(user));

        // немає підходящих rental
        when(userRentalRepository.findAll()).thenReturn(new ArrayList<>());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userRentalService.returnDisk(1L, "test@example.com");
        });

        assertEquals("The user has not rented a disk with this Id", exception.getMessage());
    }

    @Test
    void getAllRentals_shouldReturnList() {
        when(userRentalRepository.findAll()).thenReturn(List.of(new UserRental(), new UserRental()));
        List<UserRental> result = userRentalService.getAllRentals();
        assertEquals(2, result.size());
    }

    @Test
    void getAllUnavailableDisks_shouldReturnUnavailableDisks() {
        Disk availableDisk = new Disk();
        availableDisk.setAvailable(true);
        Disk unavailableDisk = new Disk();
        unavailableDisk.setAvailable(false);

        when(diskRepository.findByAvailableFalse()).thenReturn(List.of(unavailableDisk));

        List<Disk> result = userRentalService.getAllUnavailableDisks();

        assertEquals(1, result.size());
        assertFalse(result.getFirst().isAvailable());
    }
}
