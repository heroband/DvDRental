package org.example.dvdrental;

import org.example.dvdrental.controllers.AdminController;
import org.example.dvdrental.models.Disk;
import org.example.dvdrental.models.User;
import org.example.dvdrental.models.UserRental;
import org.example.dvdrental.services.UserRentalService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@Import(AdminControllerTest.MockServiceConfig.class)
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRentalService userRentalService;

    static class MockServiceConfig {
        @Bean
        public UserRentalService userRentalService() {
            return Mockito.mock(UserRentalService.class);
        }
    }

    @Test
    void showAllRentals_shouldReturnAdminRentalsViewWithRentals() throws Exception {
        Disk disk = new Disk();
        disk.setId(1L);
        disk.setPricePerDay(BigDecimal.valueOf(3.00));
        disk.setAvailable(true);

        User user = new User();
        user.setId(1L);
        user.setEmail("admin@example.com");

        UserRental rental = new UserRental();
        rental.setId(1L);
        rental.setUser(user);
        rental.setDisk(disk);
        rental.setStartDate(LocalDate.now());
        rental.setEndDate(LocalDate.now().plusDays(2));
        rental.setTotalPrice(BigDecimal.valueOf(6.00));

        when(userRentalService.getAllRentals()).thenReturn(List.of(rental));

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-rentals"))
                .andExpect(model().attributeExists("rentals"));
    }
}
