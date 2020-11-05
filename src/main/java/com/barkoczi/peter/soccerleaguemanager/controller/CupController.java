package com.barkoczi.peter.soccerleaguemanager.controller;

import com.barkoczi.peter.soccerleaguemanager.entity.Cup;
import com.barkoczi.peter.soccerleaguemanager.model.cup.CupDetails;
import com.barkoczi.peter.soccerleaguemanager.repository.CupRepository;
import com.barkoczi.peter.soccerleaguemanager.service.CupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/cups")
public class CupController {

    @Autowired
    private CupService cupService;

    @Autowired
    private CupRepository cupRepository;


    @PostMapping("/create_cup")
    public Cup createNewCup(@RequestBody CupDetails cupDetails) {
        return cupService.createAndSaveNewCup(cupDetails);
    }

    @GetMapping("/get_matches")
    public Cup getCup(@RequestParam Long cupId) {
        return cupRepository.findCupById(cupId);
    }

    @GetMapping("/list")
    public List<Cup> getCupsList(@RequestParam String locationName) {
        return cupRepository.findCupsByLocationNameOrderById(locationName);
    }

    @GetMapping("/get_cup_by_name")
    public Cup getCupByName(@RequestParam String cupName) {
        return cupRepository.findCupByName(cupName);
    }

    @DeleteMapping(value = "/{id}")
    public String deleteCup(@PathVariable("id") Long id) {
        cupService.deleteCup(id);
        return "Cup deleted";
    }
}
