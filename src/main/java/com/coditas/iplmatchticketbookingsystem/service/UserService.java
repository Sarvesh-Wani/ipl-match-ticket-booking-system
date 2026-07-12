package com.coditas.iplmatchticketbookingsystem.service;

import com.coditas.iplmatchticketbookingsystem.dto.user.LoginRequest;
import com.coditas.iplmatchticketbookingsystem.dto.user.LoginResponse;
import com.coditas.iplmatchticketbookingsystem.dto.user.RegisterRequest;
import com.coditas.iplmatchticketbookingsystem.dto.user.RegisterResponse;
import com.coditas.iplmatchticketbookingsystem.entity.RefreshToken;
import com.coditas.iplmatchticketbookingsystem.entity.User;
import com.coditas.iplmatchticketbookingsystem.enums.Role;
import com.coditas.iplmatchticketbookingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private JWTService jwtService;

    AuthenticationManager authmanager;

    RefreshTokenService refreshTokenService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JWTService jwtService, AuthenticationManager authenticationManager,
                       RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authmanager =authenticationManager;
        this.refreshTokenService=refreshTokenService;
        this.jwtService=jwtService;
    }

    public RegisterResponse registerUser(RegisterRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return RegisterResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();
    }

    public RegisterResponse registerAdmin(RegisterRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ADMIN);

        User savedUser = userRepository.save(user);

        return RegisterResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();
    }

    public LoginResponse login(LoginRequest request) {

        Authentication authentication =
                authmanager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        if (authentication.isAuthenticated()) {

            //ClassCastException - Not able to cast into User
            User user = (User) authentication.getPrincipal();

            String accessToken = jwtService.generateJwtToken(user);
            RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user);

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .RefreshToken(refreshToken.getToken())
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }

    }


    public LoginResponse refreshToken(String refreshToken) {
        RefreshToken verifiedRefreshToken = refreshTokenService.verifyRefreshToken(refreshToken);

        User user = verifiedRefreshToken.getUser();
        String accessToken = jwtService.generateJwtToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .RefreshToken(verifiedRefreshToken.getToken())
                .build();
    }

}
