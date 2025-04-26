package org.example.dvdrental.services;

import org.example.dvdrental.dto.DiskDto;
import org.example.dvdrental.models.Disk;
import org.example.dvdrental.repositories.DiskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiskService {

    private final DiskRepository diskRepository;

    public DiskService(DiskRepository diskRepository) {
        this.diskRepository = diskRepository;
    }

    public Disk addDisk(DiskDto diskDto) {
        Disk disk = new Disk();
        disk.setName(diskDto.getName());
        disk.setPricePerDay(diskDto.getPricePerDay());
        disk.setAvailable(true);
        return diskRepository.save(disk);
    }

    public List<Disk> getAllDisks() {
        return diskRepository.findAll();
    }

    public Disk getDiskById(Long id) {
        return diskRepository.findById(id).orElse(null);
    }
}
