package com.barkoczi.peter.soccerleaguemanager.service;

import com.barkoczi.peter.soccerleaguemanager.entity.League;
import com.barkoczi.peter.soccerleaguemanager.entity.Match;
import com.barkoczi.peter.soccerleaguemanager.model.league.LeagueDetails;
import com.barkoczi.peter.soccerleaguemanager.repository.LeagueRepository;
import com.barkoczi.peter.soccerleaguemanager.repository.LocationRepository;
import com.barkoczi.peter.soccerleaguemanager.service.match.GroupCreator;
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
public class LeagueService {

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private GroupCreator groupCreator;

    public League createAndSaveNewLeague(LeagueDetails leagueDetails) {
        League newLeague = League.builder()
                .name(leagueDetails.getName())
                .date(leagueDetails.getDate())
                .matchTime(leagueDetails.getMatchTime())
                .gameDay(String.join(",", leagueDetails.getGameDays()))
                .location(locationRepository.findLocationByName(leagueDetails.getLocationName()))
                .build();
        leagueRepository.saveAndFlush(newLeague);

        League league = leagueRepository.findLeagueByLocationNameAndName(leagueDetails.getLocationName(), leagueDetails.getName());
        List<List<Match>> rounds = groupCreator.createGroupMatches(
                leagueDetails.getTeams(),
                null,
                league,
                leagueDetails.getStartTime(),
                league.getMatchTime()
        );

        List<Match> all = new ArrayList<>();
        for (List<Match> round : rounds) {
            all.addAll(round);
        }
        league.setMatches(all);

        return newLeague;
    }

    public void deleteLeague(Long leagueId) {
        leagueRepository.delete(leagueRepository.findLeagueById(leagueId));
    }
}
