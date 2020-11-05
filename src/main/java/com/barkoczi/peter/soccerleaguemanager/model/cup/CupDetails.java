package com.barkoczi.peter.soccerleaguemanager.model.cup;

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
public class CupDetails {

    private String name;
    private Integer numOfTeams;
    private List<String> teamList;
    private String date;
    private String startTime;
    private String matchTime;
    private String locationName;
    private String matchType;

}
