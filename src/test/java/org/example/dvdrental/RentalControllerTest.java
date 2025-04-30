package org.example.dvdrental;

import org.example.dvdrental.controllers.RentalController;
import org.example.dvdrental.models.Disk;
import org.example.dvdrental.models.User;
import org.example.dvdrental.models.UserRental;
import org.example.dvdrental.services.UserRentalService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RentalController.class)
@Import(RentalControllerTest.MockServiceConfig.class)
public class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRentalService userRentalService;

    @TestConfiguration
    static class MockServiceConfig {
        @Bean
        public UserRentalService userRentalService() {
            return Mockito.mock(UserRentalService.class);
        }
    }

    private UserRental mockRental;

    @BeforeEach
    void setup() {
        Disk disk = new Disk();
        disk.setId(1L);
        disk.setAvailable(true);
        disk.setPricePerDay(new BigDecimal("2.50"));

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        mockRental = new UserRental();
        mockRental.setId(1L);
        mockRental.setDisk(disk);
        mockRental.setUser(user);
        mockRental.setStartDate(LocalDate.now());
        mockRental.setEndDate(LocalDate.now().plusDays(5));
        mockRental.setTotalPrice(new BigDecimal("12.50"));
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(userRentalService);
    }

    @Test
    void showRentForm_shouldReturnRentFormView() throws Exception {
        mockMvc.perform(get("/rentals/rent"))
                .andExpect(status().isOk())
                .andExpect(view().name("rent-form"))
                .andExpect(model().attributeExists("rentDiskDto"));
    }

    @Test
    void showRentForm_withDiskId_shouldSetDiskIdInModel() throws Exception {
        mockMvc.perform(get("/rentals/rent")
                        .param("diskId", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("rent-form"))
                .andExpect(model().attributeExists("rentDiskDto"))
                .andExpect(model().attribute("rentDiskDto", org.hamcrest.Matchers.hasProperty("diskId", org.hamcrest.Matchers.equalTo(10L))));
    }

    @Test
    void rentDisk_shouldReturnSuccessView_whenValidData() throws Exception {
        Mockito.when(userRentalService.rentDisk(any())).thenReturn(mockRental);

        mockMvc.perform(post("/rentals/rent")
                        .param("email", "test@example.com")
                        .param("diskId", "1")
                        .param("days", "5"))
                .andExpect(status().isOk())
                .andExpect(view().name("rental-success"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attributeExists("rental"));
    }

    @Test
    void rentDisk_shouldReturnForm_whenValidationFails() throws Exception {
        mockMvc.perform(post("/rentals/rent")
                        .param("email", "")  // Invalid
                        .param("diskId", "") // Missing
                        .param("days", "-1")) // Invalid
                .andExpect(status().isOk())
                .andExpect(view().name("rent-form"));
    }

    @Test
    void returnDisk_shouldReturnSuccess_whenValid() throws Exception {
        Mockito.when(userRentalService.returnDisk(1L, "test@example.com"))
                .thenReturn(mockRental);

        mockMvc.perform(post("/rentals/return")
                        .param("email", "test@example.com")
                        .param("diskId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rental-success"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attributeExists("rental"));
    }

    @Test
    void returnDisk_shouldShowError_whenServiceFails() throws Exception {
        Mockito.when(userRentalService.returnDisk(any(), any()))
                .thenThrow(new RuntimeException("User not found"));

        Mockito.when(userRentalService.getAllUnavailableDisks())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(post("/rentals/return")
                        .param("email", "fail@example.com")
                        .param("diskId", "99"))
                .andExpect(status().isOk())
                .andExpect(view().name("return-form"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("unavailableDisks"));
    }

    @Test
    void showReturnForm_shouldRenderView() throws Exception {
        Mockito.when(userRentalService.getAllUnavailableDisks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/rentals/return"))
                .andExpect(status().isOk())
                .andExpect(view().name("return-form"))
                .andExpect(model().attributeExists("returnDiskDto"))
                .andExpect(model().attributeExists("unavailableDisks"));
    }

    @Test
    void returnDisk_shouldReturnFormWithErrors_whenValidationFails() throws Exception {
        Mockito.when(userRentalService.getAllUnavailableDisks()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/rentals/return")
                        .param("email", "") // invalid
                        .param("diskId", "")) // invalid
                .andExpect(status().isOk())
                .andExpect(view().name("return-form"))
                .andExpect(model().attributeExists("returnDiskDto"))
                .andExpect(model().attributeExists("unavailableDisks"));
    }

}
