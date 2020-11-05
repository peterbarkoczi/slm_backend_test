package com.barkoczi.peter.soccerleaguemanager.model.team;

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
public class TeamDetails {

    private String teamName;
    private String locationName;

}
