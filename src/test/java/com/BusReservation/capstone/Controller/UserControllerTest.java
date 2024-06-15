package com.BusReservation.capstone.Controller;

import com.BusReservation.capstone.Dto.ReservationDTO;
import com.BusReservation.capstone.Dto.UserDto;
import com.BusReservation.capstone.Entity.User;
import com.BusReservation.capstone.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import java.security.Principal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserService userService;

    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        model = new BindingAwareModelMap();
    }

    @Test
    void getRegistrationPage_ShouldReturnRegisterPage() {
        // Arrange
        UserDto userDto = new UserDto();

        // Act
        String viewName = userController.getRegistrationPage(userDto);

        // Assert
        assertEquals("register", viewName);
    }

    @Test
    void saveUser_ShouldSaveUserAndReturnRegisterPage() {
        // Arrange
        UserDto userDto = new UserDto();

        // Act
        String viewName = userController.saveUser(userDto, model);

        // Assert
        assertEquals("register", viewName);
        assertEquals("Registered Successfuly!", model.getAttribute("message"));
    }

    @Test
    void login_ShouldReturnLoginPage() {
        // Act
        String viewName = userController.login();

        // Assert
        assertEquals("login", viewName);
    }

    @Test
    void userPage_ShouldReturnUserPage() {
        // Arrange
        Principal principal = () -> "john@example.com";
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("john@example.com", "password", new ArrayList<>());
        when(userDetailsService.loadUserByUsername(principal.getName())).thenReturn(userDetails);

        // Act
        String viewName = userController.userPage(model, principal);

        // Assert
        assertEquals("user", viewName);
        assertEquals(userDetails, model.getAttribute("user"));
        assertEquals("john@example.com", model.getAttribute("username"));
        assertEquals(new ReservationDTO(), model.getAttribute("reservation"));
    }

    @Test
    void adminPage_ShouldReturnAdminPage() {
        // Arrange
        Principal principal = () -> "admin@example.com";
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("admin@example.com", "password", new ArrayList<>());
        when(userDetailsService.loadUserByUsername(principal.getName())).thenReturn(userDetails);

        // Act
        String viewName = userController.adminPage(model, principal);

        // Assert
        assertEquals("admin", viewName);
        assertEquals(userDetails, model.getAttribute("user"));
    }
}

