package com.barkoczi.peter.soccerleaguemanager.service.player;

import com.barkoczi.peter.soccerleaguemanager.entity.League;
import com.barkoczi.peter.soccerleaguemanager.entity.Match;
import com.barkoczi.peter.soccerleaguemanager.entity.Player;
import com.barkoczi.peter.soccerleaguemanager.model.player.PlayerStat;
import com.barkoczi.peter.soccerleaguemanager.repository.MatchRepository;
import com.barkoczi.peter.soccerleaguemanager.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class PlayerStatCreator {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MatchRepository matchRepository;

    public PlayerStat getPlayerStat(League league, Player player) {
        Map<String, Integer> map = Stream.of(new Object[][] {
                { "Sárga", 0 },
                { "Piros", 0 },
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

        PlayerStat tempPlayerStat = PlayerStat.builder()
                .player(player)
                .goals(0)
                .cards(map)
                .build();
        List<Match> matches = matchRepository.findMatchesByLeagueIdOrderById(league.getId());
        for (Match match : matches) {
            countGoalsAndCardsInMatch(match, player.getId(), tempPlayerStat);
        }
        return tempPlayerStat;
    }

    private void countGoalsAndCardsInMatch(Match match, Long playerId, PlayerStat playerStat) {
        if (match.isFinished()) {
            countGoals(match, playerId, playerStat);
            countCards(match, playerId, playerStat);
        }
    }

    private void countGoals(Match match, Long playerId, PlayerStat playerStat) {
        int goals;
        if (match.getScorer1().contains(String.valueOf(playerId))) {
            goals = StringUtils.countOccurrencesOf(match.getScorer1(), String.valueOf(playerId));
            playerStat.setGoals(playerStat.getGoals() + goals);
        } else if (match.getScorer2().contains(String.valueOf(playerId))) {
            goals = StringUtils.countOccurrencesOf(match.getScorer2(), String.valueOf(playerId));
            playerStat.setGoals(playerStat.getGoals() + goals);
        }
    }

    private void countCards(Match match, Long playerId, PlayerStat playerStat) {
        String cardType;
        if (match.getCard1().contains(String.valueOf(playerId))) {
            for (String sub : match.getCard1().split("\n")) {
                if (sub.contains(String.valueOf(playerId))) {
                    cardType = StringUtils.trimAllWhitespace(sub.split("-")[0]);
                    playerStat.getCards().put(cardType, playerStat.getCards().get(cardType) + 1);
                }
            }
        } else if (match.getCard2().contains(String.valueOf(playerId))) {
            for (String sub : match.getCard2().split("\n")) {
                if (sub.contains(String.valueOf(playerId))) {
                    cardType = StringUtils.trimAllWhitespace(sub.split("-")[0]);
                    playerStat.getCards().put(cardType, playerStat.getCards().get(cardType) + 1);
                }
            }
        }

    }
}
