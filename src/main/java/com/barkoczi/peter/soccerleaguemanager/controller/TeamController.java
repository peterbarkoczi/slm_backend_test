package com.barkoczi.peter.soccerleaguemanager.controller;

import com.barkoczi.peter.soccerleaguemanager.entity.Team;
import com.barkoczi.peter.soccerleaguemanager.model.team.TeamDetails;
import com.barkoczi.peter.soccerleaguemanager.repository.TeamRepository;
import com.barkoczi.peter.soccerleaguemanager.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TeamService teamService;

    @GetMapping("")
    public List<Team> getLocationTeams(@RequestParam() String locationName) {
        return teamRepository.findTeamsByLocationNameOrderByNameAsc(locationName);
    }

    @PostMapping("/add_team")
    public Team addNewTeam(@RequestBody TeamDetails teamDetails) {
        return teamService.addTeam(teamDetails);
    }

    @DeleteMapping(value = "/{id}")
    public String deleteCup(@PathVariable("id") Long id) {
        teamService.deleteTeam(id);
        return "Team deleted";
    }
}
