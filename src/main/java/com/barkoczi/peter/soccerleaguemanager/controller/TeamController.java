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

    @GetMapping("/all")
    public List<Team> getAllTeam() {
        return teamRepository.findAll();
    }

    @GetMapping("")
    public List<Team> getLocationTeams(@RequestParam() String locationName) {
        return teamRepository.findTeamsByLocationNameOrderByNameAsc(locationName);
    }

    @GetMapping("/get_teamId")
    @CrossOrigin(origins = "http://localhost:3000")
    public Long getTeamId(@RequestParam("locationName") String locationName, @RequestParam("teamName") String teamName) {
        return teamService.getTeamIdByName(locationName, teamName);
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
