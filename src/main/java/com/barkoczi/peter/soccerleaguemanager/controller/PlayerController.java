package com.barkoczi.peter.soccerleaguemanager.controller;

import com.barkoczi.peter.soccerleaguemanager.entity.Player;
import com.barkoczi.peter.soccerleaguemanager.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class PlayerController {

    @Autowired
    private PlayerService playerService;


    @GetMapping("/player/list/{teamId}")
    public List<Player> getPlayers(@PathVariable("teamId") Long teamId) {
        return playerService.getPlayersByTeamId(teamId);
    }

    @PostMapping("/player/add_player/{teamId}")
    public Player addPlayer(@PathVariable("teamId") Long teamId, @RequestBody Player player) {
        System.out.println(player.toString());
        return playerService.addPlayer(teamId, player);
    }
}