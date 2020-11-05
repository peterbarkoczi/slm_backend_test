package com.barkoczi.peter.soccerleaguemanager.controller;

import com.barkoczi.peter.soccerleaguemanager.entity.Player;
import com.barkoczi.peter.soccerleaguemanager.model.player.PlayerDetails;
import com.barkoczi.peter.soccerleaguemanager.model.player.PlayerStat;
import com.barkoczi.peter.soccerleaguemanager.service.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;


    @GetMapping("/list/{teamId}")
    public List<Player> getPlayersById(@PathVariable("teamId") Long teamId) {
        return playerService.getPlayersByTeamId(teamId);
    }

    @GetMapping("/get_players_by_team/{teamName}")
    public List<Player> getPlayersByTeam(@PathVariable("teamName") String teamName,
                                         @RequestParam("locationName") String locationName) {
        return playerService.getPlayersByTeamName(teamName, locationName);
    }

    @GetMapping("/get_players_and_stats_by_team/{teamName}")
    public List<PlayerStat> getPlayersAndStatsByTeam(@PathVariable("teamName") String teamName,
                                                            @RequestParam("locationName") String locationName,
                                                            @RequestParam("leagueName") String leagueName) {
        return playerService.getPlayersAndStatsByTeamName(teamName, locationName, leagueName);
    }

    @GetMapping("/get_player_details")
    public PlayerDetails getPlayerDetails(@RequestParam("playerId") Long playerId) {
        return playerService.getPlayerDetails(playerId);
    }

    @PostMapping("/add_player/{locationName}-{teamName}")
    public Player addPlayer(@PathVariable("locationName") String locationName,
                            @PathVariable("teamName") String teamName,
                            @RequestBody Player player) {
        return playerService.addPlayer(locationName, teamName, player);
    }

    @DeleteMapping(value = "/{playerId}")
    public String deletePlayer(@PathVariable("playerId") Long playerId) {
        playerService.deletePlayer(playerId);
        return "Player deleted";
    }
}
