package com.example.shiftwallet.service;

import com.example.shiftwallet.dao.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtService jwtService;


    @Transactional
    public TokenResponse registerUser(RegisterRequest registerRequest) {
        User user = UserMapper.mapToUser(registerRequest);
        userRepository.save(user);
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

