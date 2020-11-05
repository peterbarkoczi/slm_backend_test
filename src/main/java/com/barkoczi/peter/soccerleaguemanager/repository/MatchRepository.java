package com.barkoczi.peter.soccerleaguemanager.repository;

import com.barkoczi.peter.soccerleaguemanager.entity.Match;
import com.barkoczi.peter.soccerleaguemanager.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;


public interface MatchRepository extends JpaRepository<Match, Long> {

    Match findFirstById(Long id);

    List<Match> findMatchesByFinishedEqualsAndCupIdAndMatchType(boolean finished, Long cupId, String matchType);

    List<Match> findMatchesByFinishedEqualsAndCupIdAndMatchTypeContains(boolean finished, Long cupId, String matchType);

    List<Match> findAllByCupIdAndMatchType(Long cupId, String matchType);

    List<Match> findMatchesByCupIdAndMatchTypeContainsOrderById(Long cupId, String matchType);

    List<Match> findMatchesByLeagueIdOrderById(Long leagueId);

    boolean existsMatchByCupIdAndMatchType(Long cupId, String matchType);

    Set<Match> findDistinctByTeamsIn(Set<Team> teams);

    @Query("update Match set score1 = :score, scorer1 = :scorer where id = :id")
    @Modifying(clearAutomatically = true)
    void updateScore1(@Param("score") int score, @Param("scorer") String scorer, @Param("id") Long id);

    @Query("update Match set score2 = :score, scorer2 = :scorer where id = :id")
    @Modifying(clearAutomatically = true)
    void updateScore2(@Param("score") int score, @Param("scorer") String scorer, @Param("id") Long id);

    @Query("update Match set finished = true where id = :id")
    @Modifying(clearAutomatically = true)
    void updateFinished(@Param("id") Long id);

    @Query("select max(time) from Match where finished=true and cup.id = :cupId")
    String getMaxTime(@Param("cupId") Long cupId);

    @Query("update Match set card1 = :player where id = :id")
    @Modifying(clearAutomatically = true)
    void updateCard1(@Param("id") Long id, @Param("player") String player);

    @Query("update Match set card2 = :player where id = :id")
    @Modifying(clearAutomatically = true)
    void updateCard2(@Param("id") Long id, @Param("player") String player);
}
