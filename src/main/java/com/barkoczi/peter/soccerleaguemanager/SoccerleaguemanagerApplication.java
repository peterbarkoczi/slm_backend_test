package com.barkoczi.peter.soccerleaguemanager;

import com.barkoczi.peter.soccerleaguemanager.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SoccerleaguemanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoccerleaguemanagerApplication.class, args);
    }

}
