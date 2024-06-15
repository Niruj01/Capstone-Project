package com.BusReservation.capstone.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int PassengerId;

    @ManyToOne
    @JoinColumn(name = "busId")
    private BusData busData;

    private String name;

    @ManyToOne
    @JoinColumn(name = "emailId")
    private User user;

    private String userEmail;

    private String gender;

    private int age;

    private  String mobileNo;

    private String selectedSeat;

    private double totalFare;

}
