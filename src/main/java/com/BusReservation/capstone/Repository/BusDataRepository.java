package com.BusReservation.capstone.Repository;

import com.BusReservation.capstone.Entity.BusData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusDataRepository extends JpaRepository<BusData,Integer> {

    @Query(value = "select * from Reservation  where reservation.to_destination =:to and reservation.from_destination =:from and reservation.filter_date =:date  order By reservation.filter_date desc " , nativeQuery = true)
    List<BusData> findByToFromAndDate(String to , String from, String date);
}
