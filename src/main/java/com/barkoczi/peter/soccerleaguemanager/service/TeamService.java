package com.barkoczi.peter.soccerleaguemanager.service;

import com.barkoczi.peter.soccerleaguemanager.entity.League;
import com.barkoczi.peter.soccerleaguemanager.entity.Location;
import com.barkoczi.peter.soccerleaguemanager.entity.Team;
import com.barkoczi.peter.soccerleaguemanager.model.TeamDetails;
import com.barkoczi.peter.soccerleaguemanager.repository.LeaguesRepository;
import com.barkoczi.peter.soccerleaguemanager.repository.LocationRepository;
import com.barkoczi.peter.soccerleaguemanager.repository.TeamRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    LeaguesRepository leaguesRepository;

    @Autowired
    TeamDetails teamDetails;

    public Team addTeam(TeamDetails teamDetails) {
        return createAndSaveNewTeam(teamDetails);
    }

    private Team createAndSaveNewTeam(TeamDetails teamDetails) {
        League league = leaguesRepository.findFirstByName(teamDetails.getLeagueName());
        List<League> leagues = new ArrayList<>();
        leagues.add(league);
        Team newTeam = Team.builder()
                .name(teamDetails.getTeamName())
                .location(locationRepository.findFirstById(teamDetails.getLocationId()))
                .league(leagues)
                .build();

        teamRepository.saveAndFlush(newTeam);
        return newTeam;
    }

}