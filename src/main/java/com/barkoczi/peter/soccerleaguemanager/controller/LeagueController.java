package com.barkoczi.peter.soccerleaguemanager.controller;

import com.barkoczi.peter.soccerleaguemanager.entity.League;
import com.barkoczi.peter.soccerleaguemanager.model.LeagueDetails;
import com.barkoczi.peter.soccerleaguemanager.repository.LeagueRepository;
import com.barkoczi.peter.soccerleaguemanager.service.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class LeagueController {

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private LeagueRepository leagueRepository;

    @GetMapping("/league/get_league_list/{locationName}")
    public List<League> getLeagueListByShortName(@PathVariable("locationName") String locationName) {
        return leagueRepository.findLeaguesByLocationNameOrderById(locationName);
    }

    @PostMapping("/league/create_league")
    public League addNewLeague(@RequestBody LeagueDetails leagueDetails) {
        return leagueService.createAndSaveNewLeague(leagueDetails);
    }
}
