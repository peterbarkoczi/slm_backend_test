package com.barkoczi.peter.soccerleaguemanager.service.match;

import com.barkoczi.peter.soccerleaguemanager.entity.Cup;
import com.barkoczi.peter.soccerleaguemanager.entity.Match;
import com.barkoczi.peter.soccerleaguemanager.model.team.TeamStat;
import com.barkoczi.peter.soccerleaguemanager.repository.MatchRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class SemiFinalCreator {

    @Autowired
    private TeamStatCreator teamStatCreator;

    @Autowired
    private EliminationCreator eliminationCreator;

    @Autowired
    private MatchRepository matchRepository;

    public List<List<Match>> createSemiFinalsFromGroup(Cup cup, String matchType) {
        List<String> pairs = createSemiFinalPairs(cup.getId());
        String startTime = matchRepository.getMaxTime(cup.getId());

        return eliminationCreator.createMatches(
                pairs, cup, null, startTime,
                cup.getMatchTime(), matchType, false, true);

    }

    private List<String> createSemiFinalPairs(Long cupId) {
        List<TeamStat> group1 = teamStatCreator.createTeamStat(cupId, null, "group1");
        List<TeamStat> group2 = teamStatCreator.createTeamStat(cupId, null, "group2");
        return Stream.of(
                group1.get(0).getTeam(), group2.get(1).getTeam(),
                group1.get(1).getTeam(), group2.get(0).getTeam())
                .collect(Collectors.toList());

    }

}