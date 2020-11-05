package com.barkoczi.peter.soccerleaguemanager.controller;

import com.barkoczi.peter.soccerleaguemanager.entity.Match;
import com.barkoczi.peter.soccerleaguemanager.model.match.CardDetails;
import com.barkoczi.peter.soccerleaguemanager.model.team.TeamStat;
import com.barkoczi.peter.soccerleaguemanager.repository.MatchRepository;
import com.barkoczi.peter.soccerleaguemanager.service.match.TeamStatCreator;
import com.barkoczi.peter.soccerleaguemanager.service.match.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamStatCreator teamStatCreator;

    @GetMapping("/get_matches")
    public List<Match> getQualifiers(@RequestParam String locationName, String cupName, String matchType) {
        return matchService.getQualifiersByLocationAndCupName(locationName, cupName, matchType);
    }

    @GetMapping("/get_league_matches")
    public List<Match> getLeagueMatches(@RequestParam String locationName, String leagueName) {
        return matchService.getMatchesByLeagueName(locationName, leagueName);
    }

    @GetMapping("/create_qualifiers_next_round")
    public List<Match> createQualifiersNextRound(@RequestParam Long cupId, String matchType) {
        return matchService.createQualifiersNextRound(cupId, matchType).get(0);
    }

    @GetMapping("/create_semi_finals")
    public List<Match> createSemiFinals(@RequestParam String locationName, String cupName, String matchType) {
        if (locationName == null || cupName == null) return null;
        return matchService.createCupSemifinals(locationName, cupName, matchType);
    }

    @GetMapping("/getGroupStat")
    public List<TeamStat> getTeamStat(@RequestParam String locationName, String cupName, String leagueName, String group) {
        return matchService.getTeamStat(locationName, cupName, leagueName, group);
    }

    /*
        Update
     */

    @PatchMapping("/update_score")
    @PreAuthorize("hasAnyRole('admin', 'referee')")
    public void updateScore(@RequestBody Match match) {
        matchService.updateScore(match);
    }

    @PatchMapping("/update_finished")
    @PreAuthorize("hasRole('admin')")
    public void updateFinished(@RequestBody Match match) {
        matchService.setFinished(match);
    }

    @PatchMapping("/update_card")
    public void updateCard(@RequestBody CardDetails cardDetails) {
        matchService.updateCard(cardDetails);
    }
}
