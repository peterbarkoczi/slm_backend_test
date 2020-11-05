package com.barkoczi.peter.soccerleaguemanager.controller;

import com.barkoczi.peter.soccerleaguemanager.entity.AppUser;
import com.barkoczi.peter.soccerleaguemanager.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class UserinfoController {

    @Autowired
    private AppUserService appUserService;

//    @GetMapping("/me")
//    public String currentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        AppUser user = (AppUser) authentication.getPrincipal();
//        return user.getUsername() + "\n" + user.getRole();
//    }

    @PatchMapping("/update_user/{id}")
    @PreAuthorize("hasRole('admin')")
    public String updateUserDetails(@RequestBody AppUser user, @PathVariable("id") Long id) {
        appUserService.updateUser(user, id);
        return "Sikeresen módosítottad az adatokat";
    }

    @DeleteMapping("/delete_user/{userId}")
    @PreAuthorize("hasRole('admin')")
    public String deleteUser(@PathVariable("userId") Long userId) {
        appUserService.deleteUserById(userId);
        return "Felhasználó törölve";
    }

}
