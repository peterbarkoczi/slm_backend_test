package com.barkoczi.peter.soccerleaguemanager.model.player;

import com.barkoczi.peter.soccerleaguemanager.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Service
public class PlayerStat {

    private Player player;
    private int goals;
    private Map<String, Integer> cards;

}
