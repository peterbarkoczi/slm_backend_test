package com.barkoczi.peter.soccerleaguemanager.repository;

import com.barkoczi.peter.soccerleaguemanager.entity.Player;
import com.barkoczi.peter.soccerleaguemanager.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findTeamsByLocationNameOrderByNameAsc(String locationName);

    Set<Team> findTeamsByPlayers(Player player);

    Team findFirstById(Long id);

    Team findByName(String name);

    Team findTeamByLocationNameAndName(String locationName, String name);
}
