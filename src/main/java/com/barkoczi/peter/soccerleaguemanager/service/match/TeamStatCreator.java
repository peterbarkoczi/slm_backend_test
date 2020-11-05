package com.barkoczi.peter.soccerleaguemanager.service.match;

import com.barkoczi.peter.soccerleaguemanager.entity.Match;
import com.barkoczi.peter.soccerleaguemanager.entity.Team;
import com.barkoczi.peter.soccerleaguemanager.model.team.TeamStat;
import com.barkoczi.peter.soccerleaguemanager.repository.MatchRepository;
import com.barkoczi.peter.soccerleaguemanager.repository.TeamRepository;
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
public class TeamStatCreator {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MatchRepository matchRepository;

    public List<TeamStat> createTeamStat(Long cupId, Long leagueId, String group) {
        List<TeamStat> result = new ArrayList<>();
        List<Match> tempGroup = getMatchesByGroup(cupId, leagueId, group);
        setTeamsByGroup(tempGroup, result);

        for (TeamStat teamTeamStat : result) {
            setStatOfTeam(tempGroup, teamTeamStat);
        }

        sortGroup(result);
        return result;
    }

    private List<Match> getMatchesByGroup(Long cupId, Long leagueId, String matchType) {
        if (leagueId != null) {
            return matchRepository.findMatchesByLeagueIdOrderById(leagueId);
        } else {
            return matchRepository.findMatchesByCupIdAndMatchTypeContainsOrderById(cupId, matchType);
        }
    }

    private void setTeamsByGroup(List<Match> matches, List<TeamStat> result) {
        Set<Team> teams = new HashSet<>();
        for (Match match : matches) {
            if (!match.getTeam2().equals("free")) {
                teams.add(match.getTeams().get(0));
                teams.add(match.getTeams().get(1));
            }
        }
        for (Team team : teams) {
            TeamStat temp = TeamStat.builder().team(team.getName()).build();
            result.add(temp);
        }

    }

    private void setStatOfTeam(List<Match> matches, TeamStat teamTeamStat) {
        Team currentTeam = teamRepository.findByName(teamTeamStat.getTeam());
        for (Match match : matches) {
            if (match.isFinished() && match.getTeams().contains(currentTeam)) {
                int points = teamTeamStat.getPoint();
                int played = teamTeamStat.getPlayedMatch();
                int score = teamTeamStat.getScore();
                int receivedScore = teamTeamStat.getReceivedScore();
                teamTeamStat.setPlayedMatch(played + 1);
                if (match.getTeam1().equals(teamTeamStat.getTeam())) {
                    teamTeamStat.setScore(score + match.getScore1());
                    teamTeamStat.setReceivedScore(receivedScore + match.getScore2());
                    teamTeamStat.setPoint(setPoints(match.getScore1(), match.getScore2(), points, teamTeamStat));
                } else {
                    teamTeamStat.setScore(score + match.getScore2());
                    teamTeamStat.setReceivedScore(receivedScore + match.getScore1());
                    teamTeamStat.setPoint(setPoints(match.getScore2(), match.getScore1(), points, teamTeamStat));
                }
            }
        }
        teamTeamStat.setDifference(teamTeamStat.getScore() - teamTeamStat.getReceivedScore());
    }

    private int setPoints(int score1, int score2, int point, TeamStat teamTeamStat) {
        if (score1 > score2) {
            int win = teamTeamStat.getWin();
            teamTeamStat.setWin(win + 1);
            return point + 3;
        } else if (score1 < score2) {
            int loses = teamTeamStat.getLose();
            teamTeamStat.setLose(loses + 1);
            return point;
        } else {
            int draw = teamTeamStat.getDraw();
            teamTeamStat.setDraw(draw + 1);
            return point + 1;
        }
    }

    private void sortGroup(List<TeamStat> group) {
        Comparator<TeamStat> pointComparator = Comparator.comparing(TeamStat::getPoint)
                .thenComparing(TeamStat::getDifference)
                .thenComparing(TeamStat::getScore);
        group.sort(pointComparator.reversed());
    }
}
