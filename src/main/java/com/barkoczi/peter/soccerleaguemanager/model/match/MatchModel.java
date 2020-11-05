package com.barkoczi.peter.soccerleaguemanager.model.match;

import com.barkoczi.peter.soccerleaguemanager.entity.Match;
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
public class MatchModel {

    private List<Match> matches;
    private Long cupId;

}
