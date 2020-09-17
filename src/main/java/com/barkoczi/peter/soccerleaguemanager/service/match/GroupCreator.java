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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class GroupCreator {

    @Autowired
    private EliminationCreator eliminationCreator;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    public List<List<Match>> createGroupMatches(List<String> teamsList, Cup cup, League league, String startTime, String matchTime) {
        Random random = new Random();
        List<List<Match>> allRound = new ArrayList<>();

        if (cup != null) {
            String type;
            int index = 1;
            for (List<String> group : separateTeamsToGroup(teamsList, random)) {
                type = "group" + index;
                allRound.addAll(saveMatches(cup, league, startTime, matchTime, createGroupSchedule(group, random, cup), type));
                index++;
            }
        } else {
            allRound.addAll(saveMatches(null, league, startTime, matchTime, createGroupSchedule(teamsList, random, null), "group"));
        }
        return allRound;
    }

    private List<List<Match>> saveMatches(
            Cup cup, League league, String startTime,
            String matchTime, Map<String, List<List<String>>> schedule, String type) {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        List<List<Match>> allRound = new ArrayList<>();
        Calendar time = eliminationCreator.setTime(startTime);

        for (Map.Entry<String, List<List<String>>> round : schedule.entrySet()) {
            List<Match> roundMatches = new ArrayList<>();
            for (List<String> pair : round.getValue()) {
                Match tempMatch = Match.builder()
                        .cup(cup)
                        .league(league)
                        .date(cup.getDate())
                        .time(df.format(time.getTime()))
                        .team1(pair.get(0))
                        .team2(pair.get(1))
                        .score1(0)
                        .score2(0)
                        .scorer1("")
                        .scorer2("")
                        .card1("")
                        .card2("")
                        .finished(false)
                        .matchType(type + " - round - " + round.getKey())
                        .build();

                roundMatches.add(tempMatch);
                time.add(Calendar.MINUTE, Integer.parseInt(matchTime) + 5);
            }
            MatchService.saveMatches(allRound, roundMatches, matchRepository, teamRepository);
        }
        return allRound;
    }

    private List<List<String>> separateTeamsToGroup(List<String> teamsList, Random random) {
        List<List<String>> groups = new ArrayList<>();
        List<String> groupA = new ArrayList<>();
        while (groupA.size() < teamsList.size() / 2) {
            String team = teamsList.get(random.nextInt(teamsList.size()));
            if (!groupA.contains(team)) groupA.add(team);
        }
        teamsList.removeAll(groupA);
        groups.add(groupA);
        groups.add(teamsList);

        return groups;
    }

    private Map<String, List<List<String>>> createGroupSchedule(List<String> teams, Random random, Cup cup) {
        Map<String, List<List<String>>> allRound = new TreeMap<>();
        List<List<String>> round = new ArrayList<>();

        for (int h = 0; h < teams.size() - 1; h++) {
            round.clear();
            teams = setupTeamList(teams);
            for (int i = 0, j = teams.size() - 1; i < teams.size() / 2; i++, j--) {
                setHomeOrAway(teams, random, round, i, j);
            }
            String roundIndex = new DecimalFormat("00").format(h + 1);
            allRound.put(roundIndex, new ArrayList<>(shakeTeams(round, random)));
        }

        if (cup == null) createRematches(allRound, random).forEach(allRound::putIfAbsent);

        return allRound;
    }

    private void setHomeOrAway(List<String> teams, Random random, List<List<String>> round, int i, int j) {
        List<String> homeAway = new ArrayList<>(Arrays.asList("home", "away"));
        if (homeAway.get(random.nextInt(homeAway.size())).equals("home")) {
            round.add(Arrays.asList(teams.get(i), teams.get(j)));
        } else {
            round.add(Arrays.asList(teams.get(j), teams.get(i)));
        }
    }

    private List<String> setupTeamList(List<String> teams) {
        String[] tempPart1 = new String[1];
        String[] tempPart2 = new String[1];
        String[] tempPart3 = new String[teams.size() - 2];
        tempPart1 = teams.subList(0, 1).toArray(tempPart1);
        tempPart2 = teams.subList(teams.size() - 1, teams.size()).toArray(tempPart2);
        tempPart3 = teams.subList(1, teams.size() - 1).toArray(tempPart3);
        return Arrays.asList(Stream.concat(Stream.concat(
                Arrays.stream(tempPart1),
                Arrays.stream(tempPart2)),
                Arrays.stream(tempPart3))
                .toArray(String[]::new));
    }

    private List<List<String>> shakeTeams(List<List<String>> round, Random random) {
        for (int i = 0; i < 5; i++) {
            round.add(round.remove(random.nextInt(round.size())));
        }
        return round;
    }

    private Map<String, List<List<String>>> createRematches(Map<String, List<List<String>>> allRound, Random random) {
        Map<String, List<List<String>>> rematches = new TreeMap<>();
        List<List<String>> round = new ArrayList<>();
        int indexOfRound = allRound.size() + 1;

        for (Map.Entry<String, List<List<String>>> existRound : allRound.entrySet()) {
            round.clear();
            for (List<String> match : existRound.getValue()) {
                round.add(new ArrayList<>(changeTeamsOrder(match)));
            }
            rematches.put(String.valueOf(indexOfRound++), new ArrayList<>(shakeTeams(round, random)));
        }
        return rematches;
    }

    private List<String> changeTeamsOrder(List<String> match) {
        return Arrays.asList(match.get(1), match.get(0));
    }

}
