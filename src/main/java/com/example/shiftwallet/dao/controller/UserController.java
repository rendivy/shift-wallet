package com.example.shiftwallet.dao.controller;

import com.example.shiftwallet.dao.model.account.LoginRequest;
import com.example.shiftwallet.dao.model.account.ProfileResponse;
import com.example.shiftwallet.dao.model.account.RegistrationRequest;
import com.example.shiftwallet.dao.model.auth.TokenResponse;
import com.example.shiftwallet.entity.User;
import com.example.shiftwallet.service.UserService;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@Data
@Log4j2
public class UserController {

    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<TokenResponse> registerUser(@RequestBody RegistrationRequest requestDto) {
        return ResponseEntity.ok(userService.registerUser(requestDto));
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logoutUser(@RequestHeader("Authorization") String token) {
        userService.logoutUser(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("login")
    public ResponseEntity<TokenResponse> loginUser(@RequestBody LoginRequest requestDto) {
        return ResponseEntity.ok(userService.loginUser(requestDto));
    }

    @GetMapping("profile")
    public ResponseEntity<ProfileResponse> getUserProfile(@AuthenticationPrincipal User userDetails) {
        var userEmail = userDetails.getId();
        return ResponseEntity.ok(userService.getUserProfile(userEmail));
    }

}