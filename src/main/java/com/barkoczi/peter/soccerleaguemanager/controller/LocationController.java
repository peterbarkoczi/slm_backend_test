package com.barkoczi.peter.soccerleaguemanager.controller;

import com.barkoczi.peter.soccerleaguemanager.entity.Location;
import com.barkoczi.peter.soccerleaguemanager.repository.LocationRepository;
import com.barkoczi.peter.soccerleaguemanager.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationService locationService;

    @GetMapping("/list")
    public List<Location> leagueList() {
        return locationRepository.findAll();
    }

    @PostMapping("/add_location")
    public Location addNewLocation(@RequestBody Location location) {
        return locationService.createAndSaveNewLocation(location);
    }

    @DeleteMapping(value = "/{id}")
    public String deleteLocation(@PathVariable("id") Long id) {
        locationService.deleteLocation(id);
        return "Location deleted";
    }

}
