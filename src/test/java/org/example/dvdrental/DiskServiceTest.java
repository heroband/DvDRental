package org.example.dvdrental;

import org.example.dvdrental.dto.DiskDto;
import org.example.dvdrental.models.Disk;
import org.example.dvdrental.repositories.DiskRepository;
import org.example.dvdrental.services.DiskService;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DiskServiceTest {
    @Mock
    private DiskRepository diskRepository;

    @InjectMocks
    private DiskService diskService;

    private DiskDto diskDto;

    @BeforeEach
    void setUp() {
        diskDto = new DiskDto();
        diskDto.setName("Test Movie");
        diskDto.setPricePerDay(new BigDecimal("2.50"));
    }

    @Test
    void addDisk_shouldSaveAndReturnDisk(){
        Disk savedDisk = createDisk(1L, "Test Movie", true, new BigDecimal("2.50"));

        when(diskRepository.save(any(Disk.class))).thenReturn(savedDisk);

        // Викликаю метод, який тестую
        Disk result = diskService.addDisk(diskDto);
        assertNotNull(result);
        assertEquals("Test Movie", result.getName());
        assertEquals(new BigDecimal("2.50"), result.getPricePerDay());
        assertTrue(result.isAvailable());

        // ArgumentCaptor - перехопити аргумент, який передав в save()
        ArgumentCaptor<Disk> captor = ArgumentCaptor.forClass(Disk.class);
        verify(diskRepository).save(captor.capture()); // чи викликався save()

        Disk capturedDisk = captor.getValue();

        assertEquals("Test Movie", capturedDisk.getName());
        assertEquals(new BigDecimal("2.50"), capturedDisk.getPricePerDay());
        assertTrue(capturedDisk.isAvailable());
    }

    @Test
    void getAllDisks_shouldReturnListOfDisks(){
        List<Disk> disks = Arrays.asList(
                createDisk(1L, "Movie 1", true, new BigDecimal("2.00")),
                createDisk(2L, "Movie 2", false, new BigDecimal("4.00"))
        );

        when(diskRepository.findAll()).thenReturn(disks);

        List<Disk> result = diskService.getAllDisks();

        assertEquals(2, result.size());
        assertEquals("Movie 1", result.get(0).getName());
        assertEquals("Movie 2", result.get(1).getName());
    }

    @Test
    void getAllAvailableDisks_shouldReturnOnlyAvailableDisks(){
        List<Disk> availableDisks = List.of(
                createDisk(1L, "Available 1", true, new BigDecimal("2.00")),
                createDisk(2L, "Available 2", true, new BigDecimal("4.00"))
        );

        when(diskRepository.findByAvailableTrue()).thenReturn(availableDisks);
        List<Disk> result = diskService.getAllAvailableDisks();

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(Disk::isAvailable));
    }

    @Test
    void getDiskById_shouldReturnDiskIfExists() {
        Disk disk = createDisk(1L, "Existing Disk", true, new BigDecimal("2.00"));

        when(diskRepository.findById(1L)).thenReturn(Optional.of(disk));
        Disk result = diskService.getDiskById(1L);

        assertNotNull(result);
        assertEquals("Existing Disk", result.getName());
    }

    @Test
    void getDiskById_shouldReturnNullIfNotFound(){
        when(diskRepository.findById(99L)).thenReturn(Optional.empty());
        Disk result = diskService.getDiskById(99L);
        assertNull(result);
    }

    private Disk createDisk(Long id, String name, boolean available, BigDecimal pricePerDay) {
        Disk disk = new Disk();
        disk.setId(id);
        disk.setName(name);
        disk.setAvailable(available);
        disk.setPricePerDay(pricePerDay);
        return disk;
    }
}
