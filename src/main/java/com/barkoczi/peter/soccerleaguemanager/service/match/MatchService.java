package com.barkoczi.peter.soccerleaguemanager.service.match;

import com.barkoczi.peter.soccerleaguemanager.entity.Cup;
import com.barkoczi.peter.soccerleaguemanager.entity.Match;
import com.barkoczi.peter.soccerleaguemanager.entity.Team;
import com.barkoczi.peter.soccerleaguemanager.model.CardDetails;
import com.barkoczi.peter.soccerleaguemanager.model.GroupStat;
import com.barkoczi.peter.soccerleaguemanager.repository.CupRepository;
import com.barkoczi.peter.soccerleaguemanager.repository.MatchRepository;
import com.barkoczi.peter.soccerleaguemanager.repository.TeamRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private CupRepository cupRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EliminationCreator eliminationCreator;

    @Autowired
    private GroupCreator groupCreator;


    public List<List<Match>> createQualifierMatches(List<String> teamsList, Cup cup, String startTime, String matchTime, String matchType) {
        if (matchType.equals("group")) {
            return groupCreator.createGroupMatches(teamsList, cup, null, startTime, matchTime);
        } else {
            return eliminationCreator.createMatches(teamsList, cup, null, startTime, matchTime, matchType, false, false);
        }
    }

    public List<List<Match>> createQualifiersNextRound(Long cupId, String matchType) {
        if (!matchRepository.existsMatchByCupIdAndMatchType(cupId, setMatchType(matchType))) {
            System.out.println("create new round");
            List<String> teams = eliminationCreator.createPairs(matchType, matchRepository.findAllByCupIdAndMatchType(cupId, matchType));
            Cup cup = cupRepository.findCupById(cupId);
            String matchTime = cup.getMatchTime();
            String startTime = matchRepository.getMaxTime(cupId);

            return eliminationCreator.createMatches(teams, cup, null, startTime, matchTime, setMatchType(matchType), true, false);
        }
        return null;
    }

    public List<List<Match>> createSemiFinals(Long cupId, String matchType) {
        List<Match> matches = new ArrayList<>();
        if (matchRepository.findAllByCupIdAndMatchType(cupId, matchType).isEmpty()) {
            Cup cup = cupRepository.findCupById(cupId);
            matches = eliminationCreator.getMatches(cupId, matchType);
            if (matches.size() < eliminationCreator.setMatchNumber(matchType)) {
                System.out.println("Not finished all match");
                return null;
            } else {
                String startTime = matchRepository.getMaxTime(cupId);
                List<String> teams = eliminationCreator.createPairs(matchType, matches);
                return eliminationCreator.createMatches(teams, cup, null, startTime, cup.getMatchTime(), matchType, false, false);
            }
        }
        return null;
    }

    private String setMatchType(String matchType) {
        return switch (matchType) {
            case "qualifier-1/16" -> "qualifier-1/8";
            case "qualifier-1/8" -> "qualifier-1/4";
            default -> null;
        };
    }

    static void saveMatches(List<List<Match>> allMatch, List<Match> matches, MatchRepository matchRepository, TeamRepository teamRepository) {
        for (Match match : matches) {
            matchRepository.saveAndFlush(match);
        }
        for (Match match : matches) {
            Match tempMatch = matchRepository.getOne(match.getId());
            Team tempTeam1 = teamRepository.findByName(tempMatch.getTeam1());
            Team tempTeam2 = teamRepository.findByName(tempMatch.getTeam2());
            List<Match> matches1 = tempTeam1.getMatches();
            List<Match> matches2 = tempTeam2.getMatches();
            matches1.add(tempMatch);
            matches2.add(tempMatch);
            tempTeam1.setMatches(matches1);
            tempTeam2.setMatches(matches2);
            teamRepository.saveAndFlush(tempTeam1);
            teamRepository.saveAndFlush(tempTeam2);
        }
        allMatch.add(matches);
    }

    /* Update methods */

    @Transactional
    public void updateScore(Match match) {
        String scorer;
        if (match.getScore1() != null) {
            scorer = matchRepository.findFirstById(match.getId()).getScorer1();
            scorer += match.getScorer1() + "\r\n";
            matchRepository.updateScore1(match.getScore1(), scorer, match.getId());
        } else if (match.getScore2() != null) {
            scorer = matchRepository.findFirstById(match.getId()).getScorer2();
            scorer += match.getScorer2() + "\r\n";
            matchRepository.updateScore2(match.getScore2(), scorer, match.getId());
        }
    }

    @Transactional
    public void setFinished(Match match) {
        matchRepository.updateFinished(match.getId());
    }

    @Transactional
    public void updateCard(CardDetails cardDetails) {
        String player;
        if (!cardDetails.getCard1().equals("")) {
            player = matchRepository.findFirstById(cardDetails.getId()).getCard1();
            player += cardDetails.getType() + " - " + cardDetails.getCard1() + "\r\n";
            matchRepository.updateCard1(cardDetails.getId(), player);
        } else if (!cardDetails.getCard2().equals("")) {
            player = matchRepository.findFirstById(cardDetails.getId()).getCard2();
            player += cardDetails.getType() + " - " + cardDetails.getCard2() + "\r\n";
            matchRepository.updateCard2(cardDetails.getId(), player);
        }
    }

    /* Create group table */

    Map<String, GroupStat> createGroupTable(Cup cup) {
        List<Match> group1 = matchRepository.findAllByCupIdAndMatchType(cup.getId(), "group1");
        List<Match> group2 = matchRepository.findAllByCupIdAndMatchType(cup.getId(), "group2");

        GroupStat group1Details = new GroupStat();
        Set<String> teams = new TreeSet<>();
        Map<String, Map<String, String>> details = new TreeMap<>();

        for (Match match : group1) {
            teams.add(match.getTeam1());
            teams.add(match.getTeam2());
        }

        for (String team : teams) {
            details.put(team, new TreeMap<>());
        }

        for (Match match : group1) {
            for (Map.Entry<String, Map<String, String>> team : details.entrySet()) {
                if (match.getTeam1().equals(team.getKey())) {
                    details.get(team.getKey()).put("Score", String.valueOf(match.getScore1()));
                    details.get(team.getKey()).put("Given", String.valueOf(match.getScore2()));
                } else {
                    details.get(team.getKey()).put("Score", String.valueOf(match.getScore2()));
                    details.get(team.getKey()).put("Given", String.valueOf(match.getScore1()));
                }
            }
        }


        return null;
    }

}
