package com.barkoczi.peter.soccerleaguemanager.controller;

import com.barkoczi.peter.soccerleaguemanager.entity.AppUser;
import com.barkoczi.peter.soccerleaguemanager.model.user.SigninCredentials;
import com.barkoczi.peter.soccerleaguemanager.model.user.SignupCredentials;
import com.barkoczi.peter.soccerleaguemanager.repository.AppUserRepository;
import com.barkoczi.peter.soccerleaguemanager.security.JwtTokenServices;
import com.barkoczi.peter.soccerleaguemanager.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenServices jwtTokenServices;

    @Autowired
    private AppUserService appUserService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenServices jwtTokenServices, AppUserRepository users) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenServices = jwtTokenServices;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninCredentials data) {
        try {
            String username = data.getUsername();

            Authentication authentication = authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(username, data.getPassword()));
            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String token = jwtTokenServices.createToken(username, roles);
            Long teamId = appUserService.getTeamId(username);

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("roles", roles);
            model.put("token", token);
            model.put("teamId", teamId);
            return ResponseEntity.ok(model);
        } catch (AuthenticationException e) {
//            throw new BadCredentialsException("Invalid username/password supplied");
            return ResponseEntity.badRequest().body("Hibás felhasználónév vagy jelszó");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupCredentials credentials) {
        if (appUserService.isUserExists(credentials.getUsername())) {
            return ResponseEntity.badRequest().body("Ez a felhasználónév már létezik");
        }
        appUserService.createAndSaveNewAppUser(credentials);
        return ResponseEntity.ok("Sikeres regisztráció");
    }

    @GetMapping("/getUsers")
    public List<AppUser> getUsers() {
        return appUserService.getAllUser();
    }

}
