package com.BusReservation.capstone.Service;

import com.BusReservation.capstone.Entity.Passenger;
import com.BusReservation.capstone.Entity.User;
import com.BusReservation.capstone.Repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    public Passenger addPassenger(Passenger passenger){
        return passengerRepository.save(passenger);
    }

    public List<Passenger> displayAll(){
        return passengerRepository.findAll();
    }

    public List<Passenger> findEmail(String email){
        return passengerRepository.findByUserEmail(email);
    }

    public List<Passenger> findBookingsByUser(Optional<User> user) {
        return passengerRepository.findByUser(user);
    }

    public List<Integer> SelectedSeatsByBusId(int busId) {
        try {
            List<Passenger> passengers = passengerRepository.findByBusId(busId);

            // Process passengers and collect selected seats
            List<Integer> selectedSeats = passengers.stream()
                    .map(Passenger::getSelectedSeat)  // Extract selected seats
                    .filter(Objects::nonNull)  // Filter out null values
                    .filter(seats -> !seats.isEmpty())  // Filter out empty strings
                    .flatMap(seats -> {
                        // Replace non-numeric characters with spaces
                        String cleanedSeats = seats.replaceAll("[^0-9]+", " ");

                        // Use a list to collect seat numbers
                        List<Integer> seatNumbers = new ArrayList<>();

                        // Find all numeric sequences and parse them into integers
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(cleanedSeats);
                        while (matcher.find()) {
                            seatNumbers.add(Integer.parseInt(matcher.group()));
                        }

                        // Return a stream of seat numbers
                        return seatNumbers.stream();
                    })
                    .collect(Collectors.toList());  // Collect the results into a list

            return selectedSeats;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch selected seats for bus with ID: " + busId, e);
        }
    }
}
