package org.example.dvdrental;

import org.example.dvdrental.controllers.DiskController;
import org.example.dvdrental.dto.DiskDto;
import org.example.dvdrental.models.Disk;
import org.example.dvdrental.services.DiskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(DiskController.class)
@Import(DiskControllerTest.MockServiceConfig.class)
public class DiskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DiskService diskService;

    static class MockServiceConfig {
        @Bean
        public DiskService diskService() {
            return Mockito.mock(DiskService.class);
        }
    }

    @Test
    void getAllDisks_shouldReturnDisksListView() throws Exception {
        Disk disk = new Disk();
        disk.setId(1L);
        disk.setName("Test Disk");
        disk.setPricePerDay(BigDecimal.valueOf(2.99));

        when(diskService.getAllAvailableDisks()).thenReturn(List.of(disk));

        mockMvc.perform(get("/disks"))
                .andExpect(status().isOk())
                .andExpect(view().name("disks-list"))
                .andExpect(model().attributeExists("disks"));
    }

    @Test
    void showAddForm_shouldReturnDiskFormView() throws Exception {
        mockMvc.perform(get("/disks/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("disk-form"))
                .andExpect(model().attributeExists("diskDto"));
    }

    @Test
    void addDisk_withValidDto_shouldRedirect() throws Exception {
        mockMvc.perform(post("/disks")
                        .param("name", "New Disk")
                        .param("pricePerDay", "4.99")
                        .param("available", "true")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/disks"));

        verify(diskService, times(1)).addDisk(any(DiskDto.class));
    }

    @Test
    void addDisk_withInvalidDto_shouldReturnFormView() throws Exception {
        mockMvc.perform(post("/disks")
                        .param("name", "")  // Пустой заголовок — ошибка валидации
                        .param("pricePerDay", "")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("disk-form"));
    }
}
