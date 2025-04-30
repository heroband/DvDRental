package org.example.dvdrental.controllers;

import jakarta.validation.Valid;
import org.example.dvdrental.dto.DiskDto;
import org.example.dvdrental.models.Disk;
import org.example.dvdrental.services.DiskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/disks")
public class DiskController {

    private final DiskService diskService;

    public DiskController(DiskService diskService) {
        this.diskService = diskService;
    }

    @GetMapping
    public String getAllDisks(Model model) {
        List<Disk> disks = diskService.getAllAvailableDisks();
        model.addAttribute("disks", disks);

        return "disks-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("diskDto", new DiskDto());
        return "disk-form";
    }

    @PostMapping
    public String addDisk(@ModelAttribute("diskDto") @Valid DiskDto diskDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "disk-form";
        }

        diskService.addDisk(diskDto);
        return "redirect:/disks";
    }
}
