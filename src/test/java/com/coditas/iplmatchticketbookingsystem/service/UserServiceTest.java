package com.coditas.iplmatchticketbookingsystem.service;

import com.coditas.iplmatchticketbookingsystem.dto.user.LoginRequest;
import com.coditas.iplmatchticketbookingsystem.dto.user.LoginResponse;
import com.coditas.iplmatchticketbookingsystem.dto.user.RegisterRequest;
import com.coditas.iplmatchticketbookingsystem.dto.user.RegisterResponse;
import com.coditas.iplmatchticketbookingsystem.entity.RefreshToken;
import com.coditas.iplmatchticketbookingsystem.entity.User;
import com.coditas.iplmatchticketbookingsystem.enums.Role;
import com.coditas.iplmatchticketbookingsystem.repository.UserRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private AuthenticationManager authmanager;

    @Mock
    private Authentication authentication;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private JWTService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void RegisterUserTest() {

        RegisterRequest request = new RegisterRequest(
                "vaibhav", "vaibhav123", "v@gmail.com", "v@123"
        );

        User savedUser = new User(1, "vaibhav", "vaibhav123", "v@gmail.com", "v@123", Role.USER, null, null);

        RegisterResponse mockResponse = new RegisterResponse(
                1, "vaibhav", "vaibhav123", "v@gmail.com", Role.USER.toString()
        );

        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("V@123");
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(savedUser);
        RegisterResponse response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals("vaibhav", response.getName());
    }

    @Test
    public void RegisterAdminTest() {

        RegisterRequest request = new RegisterRequest(
                "vaibhav", "vaibhav123", "v@gmail.com", "v@123"
        );

        User savedUser = new User(1, "vaibhav", "vaibhav123", "v@gmail.com", "v@123", Role.USER, null, null);

        RegisterResponse mockResponse = new RegisterResponse(
                1, "vaibhav", "vaibhav123", "v@gmail.com", Role.USER.toString()
        );

        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("V@123");
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(savedUser);
        RegisterResponse response = userService.registerAdmin(request);

        assertNotNull(response);
        assertEquals("vaibhav", response.getName());
    }


    @Test
    public void LoginTest() {

        LoginRequest request = new LoginRequest(
                "vaibhav", "v@123"
        );

        User user = new User();
        user.setUsername("hello");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("token");

        LoginResponse mockresponse = new LoginResponse(
                "accessToken", "Refreshtoken"
        );

        Mockito.when(authmanager.authenticate(Mockito.any())).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        Mockito.when(jwtService.generateJwtToken(Mockito.any())).thenReturn("Jwt-token");
        Mockito.when(refreshTokenService.generateRefreshToken(Mockito.any())).thenReturn(refreshToken);

        LoginResponse response = userService.login(request);

        assertNotNull(response);
        assertEquals("Jwt-token", response.getAccessToken());
    }


}
