package com.BusReservation.capstone.Controller;

import com.BusReservation.capstone.Entity.BusData;
import com.BusReservation.capstone.Entity.Passenger;
import com.BusReservation.capstone.Service.BusDataService;
import com.BusReservation.capstone.Service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private BusDataService busDataService;

    @Autowired
    private PassengerService passengerService;

    @GetMapping("/viewBusData")

    public String showAllBus(Model model){
        List<BusData> busData = busDataService.displayAll();
        model.addAttribute("bus",busData);

        return "viewBus";
    }

    @GetMapping("/addBusData")

    public String getAddBusForm(){
        return "addBus";
    }

    @PostMapping("/addBusData")
    public String addBus(@ModelAttribute BusData busData ){

        busDataService.saveBus(busData);
        return "redirect:/viewBusData";
    }

    @GetMapping("/updateBus/{busId}")
    public String showBusUpdateForm(@PathVariable(value = "busId") int busId, Model model){
        BusData busUpdate = busDataService.findById(busId);
        model.addAttribute("bus",busUpdate);
        return "updateBus";
    }

    @PostMapping ("/updateBus/{busId}")
    public String updateBus(@PathVariable (value = "busId") int busId,@ModelAttribute("bus") BusData busData) {
        busDataService.updateBusData(busId,busData);

        return "redirect:/viewBusData";
    }

    @GetMapping("/deleteBus/{busId}")
    public String deleteBus(@PathVariable (value = "busId") int busId,Model model) {

        // call delete Bus method
        BusData busDelete = busDataService.deleteBus(busId);
        model.addAttribute("bus",busDelete);
        return "redirect:/viewBusData";
    }

    @GetMapping("/viewAllBookings")

    public String findALlViews(@ModelAttribute("bus")BusData busData, Model model){

        List<Passenger> passengers = passengerService.displayAll();

        model.addAttribute("bus",busData);

        model.addAttribute("bookings",passengers);

        return "viewAllBookings";
    }


}
