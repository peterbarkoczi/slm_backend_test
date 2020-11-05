package com.barkoczi.peter.soccerleaguemanager.service.match;

import com.barkoczi.peter.soccerleaguemanager.entity.Cup;
import com.barkoczi.peter.soccerleaguemanager.entity.League;
import com.barkoczi.peter.soccerleaguemanager.entity.Match;
import com.barkoczi.peter.soccerleaguemanager.repository.MatchRepository;
import com.barkoczi.peter.soccerleaguemanager.repository.TeamRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class EliminationCreator {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    List<List<Match>> createMatches(List<String> teamsList,
                                    Cup cup,
                                    League league,
                                    String startTime,
                                    String matchTime,
                                    String matchType,
                                    boolean isQualifierNextRound,
                                    boolean isGroupMatch) {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");

        List<List<Match>> allMatch = new ArrayList<>();
        List<String> temp;
        Calendar time = setTime(startTime);
        List<Match> matches = new ArrayList<>();
        int range = teamsList.size() / 2;

        if (matchType.equals("semiFinal") || matchType.equals("final") || isQualifierNextRound) {
            time.add(Calendar.MINUTE, Integer.parseInt(matchTime) + 10);
        }

        for (int i = 0; i < range; i++) {
            if (matchType.contains("qualifier")) {
                temp = getRandomTeams(teamsList);
            }
//            else if (isGroupMatch) {
//                temp = teamsList;
//            }
            else {
                temp = getFirstTwoTeams(teamsList);
            }
            Match newMatch = Match.builder()
                    .cup(cup)
                    .league(league)
                    .date(cup.getDate())
                    .time(df.format(time.getTime()))
                    .team1(temp.get(0))
                    .team2(temp.get(1))
                    .score1(0)
                    .score2(0)
                    .scorer1("")
                    .scorer2("")
                    .card1("")
                    .card2("")
                    .finished(false)
                    .matchType(matchType)
                    .teams(new ArrayList<>())
                    .build();
            matches.add(newMatch);
            deleteTeams(teamsList, temp);
            time.add(Calendar.MINUTE, Integer.parseInt(matchTime) + 5);
        }
        MatchService.saveMatches(allMatch, matches, matchRepository, teamRepository);
        return allMatch;
    }

    private List<String> getRandomTeams(List<String> list) {

        Random random = new Random();
        List<String> teams = new ArrayList<>();

        while (teams.size() < 2) {
            int rand = random.nextInt(list.size());
            if (!teams.contains(list.get(rand))) {
                teams.add(list.get(rand));
            }
        }

        return teams;
    }

    private List<String> getFirstTwoTeams(List<String> teamList) {
        List<String> teams = new ArrayList<>();

        teams.add(teamList.get(0));
        teams.add(teamList.get(1));

        return teams;
    }

    private void deleteTeams(List<String> list, List<String> temp) {
        for (String item : temp) {
            list.remove(item);
        }
    }

    Calendar setTime(String startTime) {
        String[] splitTime = startTime.split(":");
        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTime[0].replaceAll("\\s+", "")));
        time.set(Calendar.MINUTE, Integer.parseInt(splitTime[1].replaceAll("\\s+", "")));
        return time;
    }

    List<String> createPairs(String matchType, List<Match> matches) {
        List<String> teams;
        List<String> winners = new ArrayList<>();
        List<String> losers = new ArrayList<>();
        for (Match match : matches) {
            if (match.getScore1() > match.getScore2()) {
                winners.add(match.getTeam1());
                losers.add(match.getTeam2());
            } else {
                winners.add(match.getTeam2());
                losers.add(match.getTeam1());

            }
        }
        teams = getTeams(matchType, winners, losers);
        return teams;
    }

    private List<String> getTeams(String matchType, List<String> winners, List<String> losers) {
        if (matchType.equals("final")) {
            return Stream.concat(losers.stream(), winners.stream())
                    .collect(Collectors.toList());
        } else {
            return winners;
        }
    }

    int setMatchNumber(String matchType) {
        if (matchType.equals("final")) {
            return 2;
        }
        return 4;
    }

    List<Match> getMatches(Long cupId, String matchType) {
        if (matchType.equals("semiFinal")) {
            return matchRepository.findMatchesByFinishedEqualsAndCupIdAndMatchTypeContains(true, cupId, "qualifier-1/4");
        } else {
            return matchRepository.findMatchesByFinishedEqualsAndCupIdAndMatchType(true, cupId, "semiFinal");
        }
    }

}
