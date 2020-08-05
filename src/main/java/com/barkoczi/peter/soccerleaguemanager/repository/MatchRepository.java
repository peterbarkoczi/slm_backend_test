package com.barkoczi.peter.soccerleaguemanager.repository;

import com.barkoczi.peter.soccerleaguemanager.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findAllByCupId(Long cupId);

    Match findFirstById(Long id);

//    @Query("SELECT :scorer from Match where id = :id")
//    String getScorer(@Param("scorer") String scorer, @Param("id") Long id);

    @Query("update Match set score1 = :score, scorer1 = :scorer where id = :id")
    @Modifying(clearAutomatically = true)
    void updateScore1(@Param("score") int score, @Param("scorer") String scorer, @Param("id") Long id);

    @Query("update Match set score2 = :score, scorer2 = :scorer where id = :id")
    @Modifying(clearAutomatically = true)
    void updateScore2(@Param("score") int score, @Param("scorer") String scorer, @Param("id") Long id);
}