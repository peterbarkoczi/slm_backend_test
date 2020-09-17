package com.barkoczi.peter.soccerleaguemanager.model;

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
public class TeamStat {

    private int point = 0;
    private int score = 0;
    private int receivedScore = 0;
    private int difference = 0;
    private int playedMatch = 0;
    private String team;

}
