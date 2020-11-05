package com.barkoczi.peter.soccerleaguemanager.service;

import com.barkoczi.peter.soccerleaguemanager.entity.Team;
import com.barkoczi.peter.soccerleaguemanager.model.team.TeamDetails;
import com.barkoczi.peter.soccerleaguemanager.repository.LeagueRepository;
import com.barkoczi.peter.soccerleaguemanager.repository.LocationRepository;
import com.barkoczi.peter.soccerleaguemanager.repository.TeamRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class TeamService {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    LeagueRepository leagueRepository;

    @Autowired
    TeamDetails teamDetails;

    public Team addTeam(TeamDetails teamDetails) {
        return createAndSaveNewTeam(teamDetails);
    }

    private Team createAndSaveNewTeam(TeamDetails teamDetails) {
        Team newTeam = Team.builder()
                .name(teamDetails.getTeamName())
                .location(locationRepository.findLocationByName(teamDetails.getLocationName()))
                .matches(new ArrayList<>())
                .build();

        teamRepository.saveAndFlush(newTeam);
        return newTeam;
    }

    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }

    public Long getTeamIdByName(String locationName, String teamName) {
        Team team = teamRepository.findTeamByLocationNameAndName(locationName, teamName);
        return team.getId();
    }
}
