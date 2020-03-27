package com.PoLYmer.flightreservation.controllers;

import com.PoLYmer.flightreservation.dao.ServerInterface;
import com.PoLYmer.flightreservation.flight.Flights;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/flights")
public class FlightsController {
    @GetMapping
    public ResponseEntity<Flights> get(@RequestParam(value = "teamname", defaultValue = "PoLYmer") String teamname,
                                       @RequestParam(value = "airport") String airportCode,
                                       @RequestParam(value = "listType") String listType,
                                       @RequestParam(value = "date") String date) {

        Flights flights = ServerInterface.INSTANCE.getFlights(teamname, listType, airportCode, date);
        System.out.println(new ResponseEntity<Flights>(flights, HttpStatus.OK));
        return new ResponseEntity<Flights>(flights, HttpStatus.OK);
    }
}
