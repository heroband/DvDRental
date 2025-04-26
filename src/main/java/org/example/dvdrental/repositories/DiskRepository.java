package org.example.dvdrental.repositories;

import org.example.dvdrental.models.Disk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiskRepository extends JpaRepository<Disk, Long> {
}
