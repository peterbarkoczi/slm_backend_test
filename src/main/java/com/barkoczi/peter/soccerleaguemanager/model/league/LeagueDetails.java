package com.barkoczi.peter.soccerleaguemanager.model.league;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Service
public class LeagueDetails {

    private String name;
    private List<String> teams;
    private String date;
    private String startTime;
    private List<String> gameDays;
    private String matchTime;
    private String locationName;

}
