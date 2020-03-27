package com.PoLYmer.flightreservation.controllers;

import com.PoLYmer.flightreservation.airport.Airport;
import com.PoLYmer.flightreservation.airport.Airports;
import com.PoLYmer.flightreservation.dao.ServerInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/api/v1/airports")
public class AirportsController {
    @GetMapping
    public ResponseEntity<List<String>> get(@RequestParam(value = "teamname", defaultValue = "PoLYmer") String teamname) {

        Airports airports = ServerInterface.INSTANCE.getAirports(teamname);
        List<String> codes = new ArrayList<String>();
        for (Airport airport: airports){
            codes.add(airport.getmCode());
        }
        System.out.println(new ResponseEntity<List<String>>(codes, HttpStatus.OK));
        return new ResponseEntity<List<String>>(codes, HttpStatus.OK);
    }
}
