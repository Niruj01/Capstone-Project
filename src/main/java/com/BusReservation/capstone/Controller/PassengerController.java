package com.BusReservation.capstone.Controller;

import com.BusReservation.capstone.Dto.ReservationDTO;
import com.BusReservation.capstone.Entity.BusData;
import com.BusReservation.capstone.Entity.Passenger;
import com.BusReservation.capstone.Entity.User;
import com.BusReservation.capstone.Service.BusDataService;
import com.BusReservation.capstone.Service.PassengerService;
import com.BusReservation.capstone.Service.PdfCreationService;
import com.BusReservation.capstone.Service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private BusDataService busDataService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    private PdfCreationService pdfCreationService;

    private Passenger passengerPdf;


    @GetMapping("/bookBus")
    public String findBus(@ModelAttribute("reservation") ReservationDTO reservationDTO, Model model, Principal principal) {

        List<BusData> busDataList = busDataService.findByFromTo(reservationDTO.getTo(), reservationDTO.getFrom(), reservationDTO.getFilterDate());
        if (busDataList.isEmpty()) {
            busDataList = null;
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        model.addAttribute("bus", busDataList);
        model.addAttribute("reservation", reservationDTO);


        return "user";
    }

    @GetMapping("/seatBooking")
    public String showSeatBookPage(@RequestParam("busId") int busId, Model model, Principal principal) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
            model.addAttribute("busId", busId);
            List<Integer> selectedSeats = passengerService.SelectedSeatsByBusId(busId);
            model.addAttribute("passedSeats", selectedSeats);
            return "seatBooking";
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            model.addAttribute("errorMessage", "An error occurred while calling seat booking page");
            return "error";
        }

    }

    @PostMapping("/bookSeats")
    public String bookSeats(@RequestParam("selectedSeats") String selectedSeats,
                            @RequestParam("totalFare") double totalFare,
                            @RequestParam("busId") int busId,
                            @RequestParam("seatCount") Integer seatCount,
                            Model model, Principal principal) {

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
            Passenger passenger = new Passenger();
            model.addAttribute("passenger", passenger);
            model.addAttribute("selectedSeats", selectedSeats);
            model.addAttribute("totalFare", totalFare);
            model.addAttribute("busId", busId);
            model.addAttribute("seatCount", seatCount);

            return "passenger";
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            model.addAttribute("errorMessage", "An error occurred while booking seats.");
            return "error";
        }
    }

    @PostMapping("/newPassenger")
    public String PassengerDetail(@RequestParam("selectedSeats") String selectedSeats,
                                  @RequestParam("totalFare") double totalFare,
                                  @RequestParam("busId") int busId,
                                  @RequestParam("seatCount") int seats,
                                  @ModelAttribute("passenger") Passenger passenger,
                                  @ModelAttribute("bus") BusData bus,
                                  @ModelAttribute("user")User user,
                                  Model model, Principal principal
    ) {
        try {

            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);

            model.addAttribute("bus", bus);

            model.addAttribute("logInUser", user);

            User currentUser = userServiceImp.currentUser();


            BusData busEntity = busDataService.findById(busId);


            busDataService.availableSeats(busId, seats);//Update available seats

            passenger.setBusData(busEntity);
            passenger.setName(passenger.getName());
            passenger.setUser(currentUser);



            passenger.setGender(passenger.getGender());
            passenger.setAge(passenger.getAge());
            passenger.setSelectedSeat(selectedSeats);
            passenger.setTotalFare(totalFare);

            passengerService.addPassenger(passenger);

            this.passengerPdf = passenger;


            return "pdfTemplate";
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            model.addAttribute("errorMessage", "An error occurred while Creating Passenger or" +
                    " While updating the available seats");
            return "error";
        }

    }

    // Generate PDF
    @PostMapping("/generatePdf")
    public String generatePdf(Model model) {
        Passenger passenger = this.passengerPdf;
        Map<String, Object> data = new HashMap<>();
        data.put("passenger", passenger);
        try {
            pdfCreationService.createPdf("pdfTemplate", data, "generatedPdf.pdf");
            return "redirect:/user-page";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "An error occurred while creating PDF document");
            return "error";
        }
    }

    // Download PDF
    @GetMapping("/downloadPdf")
    public ResponseEntity<byte[]> downloadPdf() throws IOException {
        File pdfFile = new File("C:\\Users\\niruj\\idea Projects\\capstone\\generatedPdf.pdf");
        byte[] pdfContent = Files.readAllBytes(pdfFile.toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Ticket.pdf");
        return ResponseEntity.ok().headers(headers).body(pdfContent);
    }


    @GetMapping("/viewBookings")
    public String findBooking(@ModelAttribute("passenger") Passenger passenger,
                              @ModelAttribute("bus") BusData bus,
                              Model model, Principal principal) {
        try {
            // Retrieve the current user's email
            String email = principal.getName();

            // Use the email to find the user
            Optional<User> currentUser = userServiceImp.findByEmail(email);

            // Use the user information to find all bookings made by this user
            List<Passenger> bookings = passengerService.findBookingsByUser(currentUser);

            model.addAttribute("bookings", bookings);
            model.addAttribute("user", currentUser);
            model.addAttribute("bus",bus);
            model.addAttribute("passenger",passenger);

            return "viewBookings";
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            model.addAttribute("errorMessage", "An error occurred while retrieving bookings.");
            return "error";
        }
    }


}






