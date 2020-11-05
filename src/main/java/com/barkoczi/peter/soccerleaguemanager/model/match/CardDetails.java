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
public class CardDetails {

    Long id;
    String card1;
    String card2;
    String type;

}
