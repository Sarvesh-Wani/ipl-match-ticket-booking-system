package com.coditas.iplmatchticketbookingsystem;

import com.coditas.iplmatchticketbookingsystem.entity.RefreshToken;
import com.coditas.iplmatchticketbookingsystem.entity.User;
import com.coditas.iplmatchticketbookingsystem.repository.UserRepository;
import com.coditas.iplmatchticketbookingsystem.service.JWTService;
import com.coditas.iplmatchticketbookingsystem.service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;

    public OAuth2SuccessHandler(UserRepository userRepository, JWTService jwtService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String providerId = oAuth2User.getName();
        String email = oAuth2User.getAttribute("email");

        String provider =
                ((OAuth2AuthenticationToken) authentication)
                        .getAuthorizedClientRegistrationId()
                        .toUpperCase();

        User user = userRepository.findByProviderAndProviderId(provider, providerId).orElseGet(
                () -> {
                    User newUser = new User();
                    newUser.setUsername(email);
                    newUser.setProvider(provider);
                    newUser.setProviderId(providerId);

                    return userRepository.save(newUser);
                }
        );

        String accessToken = jwtService.generateJwtToken(user);
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user);


        String targetUrl = UriComponentsBuilder.fromUriString("/tokens.html")
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken.getToken()) // Extract the string value
                .build().toUriString();

        response.sendRedirect(targetUrl);


    }
}
