package com.BusReservation.capstone.service;

import com.BusReservation.capstone.Entity.Passenger;
import com.BusReservation.capstone.Entity.User;
import com.BusReservation.capstone.Repository.PassengerRepository;
import com.BusReservation.capstone.Service.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class PassengerServiceTest {

    @InjectMocks
    private PassengerService passengerService;

    @Mock
    private PassengerRepository passengerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPassenger_ShouldSavePassenger() {
        // Arrange
        Passenger passenger = new Passenger();
        when(passengerRepository.save(passenger)).thenReturn(passenger);

        // Act
        Passenger savedPassenger = passengerService.addPassenger(passenger);

        // Assert
        assertEquals(passenger, savedPassenger);
    }

    @Test
    void displayAll_ShouldReturnAllPassengers() {
        // Arrange
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(new Passenger());
        when(passengerRepository.findAll()).thenReturn(passengers);

        // Act
        List<Passenger> allPassengers = passengerService.displayAll();

        // Assert
        assertEquals(passengers, allPassengers);
    }

    @Test
    void findEmail_ShouldReturnPassengersByEmail() {
        // Arrange
        String email = "test@example.com";
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(new Passenger());
        when(passengerRepository.findByUserEmail(email)).thenReturn(passengers);

        // Act
        List<Passenger> result = passengerService.findEmail(email);

        // Assert
        assertEquals(passengers, result);
    }

    @Test
    void findBookingsByUser_ShouldReturnPassengersByUser() {
        // Arrange
        User user = new User();
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(new Passenger());
        when(passengerRepository.findByUser(Optional.of(user))).thenReturn(passengers);

        // Act
        List<Passenger> result = passengerService.findBookingsByUser(Optional.of(user));

        // Assert
        assertEquals(passengers, result);
    }

    @Test
    void selectedSeatsByBusId_ShouldReturnSelectedSeats() {
        // Arrange
        int busId = 1;
        List<Passenger> passengers = new ArrayList<>();
        Passenger passenger = new Passenger();
        passenger.setSelectedSeat("1,2,3");
        passengers.add(passenger);
        when(passengerRepository.findByBusId(busId)).thenReturn(passengers);

        // Act
        List<Integer> selectedSeats = passengerService.SelectedSeatsByBusId(busId);

        // Assert
        List<Integer> expectedSeats = List.of(1, 2, 3);
        assertEquals(expectedSeats, selectedSeats);
    }
}

