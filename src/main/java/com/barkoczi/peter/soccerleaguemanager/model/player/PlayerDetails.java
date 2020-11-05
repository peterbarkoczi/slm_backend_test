package com.barkoczi.peter.soccerleaguemanager.model.player;

import com.barkoczi.peter.soccerleaguemanager.entity.Cup;
import com.barkoczi.peter.soccerleaguemanager.entity.League;
import com.barkoczi.peter.soccerleaguemanager.entity.Player;
import com.barkoczi.peter.soccerleaguemanager.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Service
public class PlayerDetails {

    private Player player;
    private Set<Team> teams;
    private Set<League> leagues;
    private Set<Cup> cups;

}
