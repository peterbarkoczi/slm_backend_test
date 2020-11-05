package com.barkoczi.peter.soccerleaguemanager.model.match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Service
public class MatchDetails {

    private String time;
    private String team1;
    private String team2;

}
