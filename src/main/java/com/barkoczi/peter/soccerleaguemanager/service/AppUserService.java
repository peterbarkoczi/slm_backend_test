package com.barkoczi.peter.soccerleaguemanager.service;

import com.barkoczi.peter.soccerleaguemanager.entity.AppUser;
import com.barkoczi.peter.soccerleaguemanager.model.user.SignupCredentials;
import com.barkoczi.peter.soccerleaguemanager.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class AppUserService {

    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Autowired
    private AppUserRepository appUserRepository;

    public boolean isUserExists(String username) {
        return appUserRepository.existsAppUserByUsername(username);
    }

    public void createAndSaveNewAppUser(SignupCredentials credentials) {
        AppUser tempUser = AppUser.builder()
                .username(credentials.getUsername())
                .password(passwordEncoder.encode(credentials.getPassword()))
                .role(credentials.getRole())
                .build();

        appUserRepository.saveAndFlush(tempUser);
    }

    public List<AppUser> getAllUser() {
        return appUserRepository.findAllByOrderByIdAsc();
    }

    public Long getTeamId(String username) {
        Optional<AppUser> user = appUserRepository.findByUsername(username);
        return user.map(AppUser::getTeamId).orElse(null);
    }

    @Transactional
    public void updateUser(AppUser user, Long userId) {
        appUserRepository.updateUserDetails(user.getUsername(), user.getRole(), user.getTeamId(), userId);
    }

    public void deleteUserById(Long userId) {
        appUserRepository.deleteById(userId);
    }
}
