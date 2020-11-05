package com.barkoczi.peter.soccerleaguemanager.service;


import com.barkoczi.peter.soccerleaguemanager.entity.Cup;
import com.barkoczi.peter.soccerleaguemanager.entity.Match;
import com.barkoczi.peter.soccerleaguemanager.model.cup.CupDetails;
import com.barkoczi.peter.soccerleaguemanager.repository.CupRepository;
import com.barkoczi.peter.soccerleaguemanager.repository.LocationRepository;
import com.barkoczi.peter.soccerleaguemanager.service.match.MatchService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class CupService {

    @Autowired
    private CupRepository cupRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MatchService matchService;

    public Cup createAndSaveNewCup(CupDetails cupDetails) {
        Cup newCup = Cup.builder()
                .name(cupDetails.getName())
                .date(cupDetails.getDate())
                .matchTime(cupDetails.getMatchTime())
                .qualifierType(cupDetails.getMatchType())
                .location(locationRepository.findLocationByName(cupDetails.getLocationName()))
                .build();
        cupRepository.saveAndFlush(newCup);

        Cup cup = cupRepository.findCupByName(cupDetails.getName());
        List<List<Match>> rounds = matchService.createQualifierMatches(
                cupDetails.getTeamList(),
                newCup,
                cupDetails.getStartTime(),
                cup.getMatchTime(),
                cupDetails.getMatchType()
        );

        List<Match> all = new ArrayList<>();
        for (List<Match> round : rounds) {
            all.addAll(round);
        }
        cup.setMatches(all);

        return newCup;
    }

    public void deleteCup(Long id) {
        cupRepository.delete(cupRepository.findCupById(id));
    }
}
