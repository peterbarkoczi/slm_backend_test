package com.barkoczi.peter.soccerleaguemanager.repository;

import com.barkoczi.peter.soccerleaguemanager.entity.Cup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CupRepository extends JpaRepository<Cup, Long> {

    Cup findCupByName(String name);

    Cup findCupById(Long id);

    Cup findCupByLocationNameAndName(String locationName, String cupName);

    List<Cup> findCupsByLocationNameOrderById(String locationName);

    Set<Cup> findCupsByIdIn(Set<Long> ids);

}
