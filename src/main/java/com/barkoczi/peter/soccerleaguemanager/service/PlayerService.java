package com.barkoczi.peter.soccerleaguemanager.service;

import com.barkoczi.peter.soccerleaguemanager.entity.*;
import com.barkoczi.peter.soccerleaguemanager.model.PlayerDetails;
import com.barkoczi.peter.soccerleaguemanager.repository.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class PlayerService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private CupRepository cupRepository;


    public Player addPlayer(String locationName, String teamName, Player player) {

        List<Team> teams = new ArrayList<>();
        teams.add(teamRepository.findTeamByLocationNameAndName(locationName, teamName));
        Player newPlayer = Player.builder()
                .number(player.getNumber())
                .name(player.getName())
                .teams(teams)
                .birthDate(player.getBirthDate())
                .goals(0L)
                .build();

        playerRepository.saveAndFlush(newPlayer);

        return newPlayer;
    }

    public List<Player> getPlayersByTeamId(Long teamId) {
        return playerRepository.findPlayersByTeams(teamRepository.findFirstById(teamId));
    }

    public List<Player> getPlayersByTeamName(String teamName) {
        return playerRepository.findPlayersByTeams(teamRepository.findByName(teamName));
    }

    public PlayerDetails getPlayerDetails(Long playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        Set<Team> teams = teamRepository.findTeamsByPlayers(player);
        Set<Long> leagueIds = new HashSet<>(), cupIds = new HashSet<>();
        Set<Match> matches = matchRepository.findDistinctByTeamsIn(teams);

        setLeagueAndCupId(matches, leagueIds, cupIds);

        return PlayerDetails.builder()
                .player(player)
                .teams(teams)
                .leagues(leagueRepository.findLeaguesByIdIn(leagueIds))
                .cups(cupRepository.findCupsByIdIn(cupIds))
                .build();
    }

    private void setLeagueAndCupId(Set<Match> matches, Set<Long> leagueIds, Set<Long> cupIds) {
        for (Match match : matches) {
            if (match.getLeague() != null) leagueIds.add(match.getLeague().getId());
            if (match.getCup() != null) cupIds.add(match.getCup().getId());
        }
    }

}
