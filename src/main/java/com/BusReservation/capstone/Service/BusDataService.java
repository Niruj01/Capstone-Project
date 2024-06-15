package com.BusReservation.capstone.Service;

import com.BusReservation.capstone.Entity.BusData;
import com.BusReservation.capstone.Repository.BusDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusDataService {

    @Autowired
    private BusDataRepository busRepo;


//    1) to store BusData in DataBase(mySql)

    public BusData saveBus(BusData busData){

        return busRepo.save(busData);
    }


//    2) Display all bus

    public List<BusData> displayAll(){
        return  busRepo.findAll();
    }

//    3)findbyid

    public  BusData findById(int busId){
        Optional<BusData> busDataId = busRepo.findById(busId);
        if(busDataId.isPresent()){
            return busDataId.get();
        }else{
            throw new RuntimeException("Bus not found with id: " + busId);
        }
    }

//    4) UpdateBus

    public void updateBusData(int busId, BusData busData){

        Optional<BusData> busDataId = busRepo.findById(busId);
        if(busDataId.isPresent()){
            BusData updatedBus = busDataId.get();
            updatedBus .setBusName(busData.getBusName());
            updatedBus .setFromDestination(busData.getFromDestination());
            updatedBus.setToDestination(busData.getToDestination());
            updatedBus.setPrice(busData.getPrice());
            updatedBus.setTime(busData.getTime());
            updatedBus.setTotalSeats(busData.getTotalSeats());
            updatedBus.setAvailableSeats(busData.getAvailableSeats());

            busRepo.save(updatedBus);
        }

    }

//    5)DeleteBus

    public BusData deleteBus(int busId){

        busRepo.deleteById(busId);
        return null;
    }

//    6)FindByFromTo

    public List<BusData> findByFromTo(String to,String from,String date){
        List<BusData>  busList =busRepo.findByToFromAndDate(to,from,date);
        return busList;
    }

//    7) update

    public void availableSeats(int busId, int seatsBooked){
        Optional<BusData> busData = busRepo.findById(busId);
        if (busData.isPresent()){
            BusData bus = busData.get();
            int availableSeats = bus.getAvailableSeats() - seatsBooked;
            if (availableSeats >=0){
                bus.setAvailableSeats(availableSeats);
                busRepo.save(bus);
            }else {
                throw new RuntimeException("Not enough available seats on the bus");
            }
        }else {
            throw new RuntimeException("Bus not found with id: " + busId);
        }
    }

}
