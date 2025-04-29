package org.example.dvdrental.controllers;

import jakarta.validation.Valid;
import org.example.dvdrental.dto.ApiResponse;
import org.example.dvdrental.dto.DiskDto;
import org.example.dvdrental.models.Disk;
import org.example.dvdrental.services.DiskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disks")
public class DiskController {

    private final DiskService diskService;

    public DiskController(DiskService diskService) {
        this.diskService = diskService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Disk>>> getAllDisks() {
        List<Disk> disks = diskService.getAllDisks();
        return ResponseEntity.ok(new ApiResponse<>("Disk list fetched", disks));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Disk>> getDiskById(@PathVariable Long id) {
        Disk disk = diskService.getDiskById(id);

        if (disk != null) {
            return ResponseEntity.ok(new ApiResponse<>("Disk fetched", disk));
        } else {
            return ResponseEntity
                    .status(404)
                    .body(new ApiResponse<>("Disk not found", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Disk>> addDisk(@RequestBody @Valid DiskDto diskDto) {
        Disk savedDisk = diskService.addDisk(diskDto);
        return ResponseEntity.ok(new ApiResponse<>("Disk added successfully", savedDisk));
    }
}
