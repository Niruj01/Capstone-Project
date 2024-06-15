package com.BusReservation.capstone.Repository;

import com.BusReservation.capstone.Entity.Passenger;
import com.BusReservation.capstone.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger,Integer> {

    @Query("SELECT p FROM Passenger p WHERE p.busData.busId = :busId")
    List<Passenger> findByBusId(@Param("busId") int busId);

    @Query("SELECT p FROM Passenger p WHERE p.user.email = :email")
    List<Passenger> findByUserEmail(@Param("email") String email);

    List<Passenger> findByUser(Optional<User> user);
}
