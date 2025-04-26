package org.example.dvdrental.controllers;

import jakarta.validation.Valid;
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
    public List<Disk> getAllDisks() {
        return diskService.getAllDisks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disk> getDiskById(@PathVariable Long id) {
        Disk disk = diskService.getDiskById(id);

        if (disk != null) {
            return ResponseEntity.ok(disk);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Disk> addDisk(@RequestBody @Valid DiskDto diskDto) {
        Disk diskSaved = diskService.addDisk(diskDto);
        return ResponseEntity.ok(diskSaved);
    }
}
