package com.barkoczi.peter.soccerleaguemanager.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupCredentials {

    private String username;
    private String password;
    private String role;

}
