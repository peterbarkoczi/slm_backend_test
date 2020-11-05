package com.barkoczi.peter.soccerleaguemanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String date;

    private String time;
    private String team1;
    private String team2;

    @Column(name = "score1", columnDefinition = "integer default 0")
    private Integer score1;

    @Column(columnDefinition = "integer default 0")
    private Integer score2;

    @Column(columnDefinition = "varchar(255) default ''")
    private String scorer1;
    @Column(columnDefinition = "varchar(255) default ''")
    private String scorer2;

    @Column(columnDefinition = "varchar(255) default ''")
    private String card1;
    @Column(columnDefinition = "varchar(255) default ''")
    private String card2;

    @Column(columnDefinition = "boolean default false")
    private boolean finished = false;

    @Column(columnDefinition = "varchar(255) default ''")
    private String matchType;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cup cup;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private League league;

    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "matches")
    private List<Team> teams;

    @PreRemove
    private void removeTeamsFromMatches() {
        for (Team team : teams) {
            team.getMatches().remove(this);
        }
    }

}
