package com.BusReservation.capstone.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name= "Reservation")
@NoArgsConstructor
@AllArgsConstructor
public class BusData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="busId")
    private int busId;

    private String filterDate;

    private String toDestination;

    private String fromDestination;

    private Double price;

    private String BusName;

    private String time;

    private int totalSeats;

    private int availableSeats;

}
