package org.example.dvdrental.repositories;

import org.example.dvdrental.models.Disk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiskRepository extends JpaRepository<Disk, Long> {
    List<Disk> findByAvailableTrue();
    List<Disk> findByAvailableFalse();
}
