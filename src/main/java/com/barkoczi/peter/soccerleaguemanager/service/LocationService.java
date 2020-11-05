package com.barkoczi.peter.soccerleaguemanager.service;

import com.barkoczi.peter.soccerleaguemanager.entity.Location;
import com.barkoczi.peter.soccerleaguemanager.repository.LocationRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;

    public Location createAndSaveNewLocation(Location location) {
        Location newLocation = Location.builder()
                .name(location.getName())
                .leagues(new ArrayList<>())
                .teams(new HashSet<>())
                .build();

        locationRepository.saveAndFlush(newLocation);

        return newLocation;
    }

    public void deleteLocation(Long id) {
        locationRepository.delete(locationRepository.findFirstById(id));
    }

}
