package com.example.shiftwallet.service;

import com.example.shiftwallet.dao.model.account.LoginRequest;
import com.example.shiftwallet.dao.model.account.ProfileResponse;
import com.example.shiftwallet.dao.model.account.RegistrationRequest;
import com.example.shiftwallet.dao.model.auth.TokenResponse;
import com.example.shiftwallet.dao.repository.RedisRepository;
import com.example.shiftwallet.dao.repository.UserRepository;
import com.example.shiftwallet.dao.repository.mappers.UserMapper;
import com.example.shiftwallet.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RedisRepository redisRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Transactional
    public TokenResponse registerUser(RegistrationRequest registerRequest) {
        User user = UserMapper.mapToUser(registerRequest, passwordEncoder::encode);
        userRepository.save(user);
        return TokenResponse.builder()
                .token(jwtService.generateToken(user.getId(), user.getEmail()))
                .build();
    }


    public void logoutUser(String token) {
        var tokenId = jwtService.extractTokenId(token.substring("Bearer ".length()));
        redisRepository.save(tokenId, "Invalid");
    }




    public TokenResponse loginUser(LoginRequest loginRequest) {
        var userOptional = userRepository.findByEmail(loginRequest.email());
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginRequest.email()));
        return TokenResponse.builder()
                .token(jwtService.generateToken(user.getId(), user.getEmail()))
                .build();
    }


    public ProfileResponse getUserProfile(UUID id) {
        var user = userRepository.findById(id);
        return UserMapper.mapToProfileDTO(user.orElseThrow());
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

}

