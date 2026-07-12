package com.coditas.iplmatchticketbookingsystem.service;

import com.coditas.iplmatchticketbookingsystem.entity.RefreshToken;
import com.coditas.iplmatchticketbookingsystem.entity.User;
import com.coditas.iplmatchticketbookingsystem.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken generateRefreshToken(User user) {

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(60*1000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }


    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken refreshTokenDB = refreshTokenRepository.findByToken(refreshToken).orElseThrow(
                () -> new RuntimeException("Refresh token does not exist in DB")
        );

        if(refreshTokenDB.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(refreshTokenDB);
            throw new RuntimeException("Refresh token is expired! Login now.......");
        }
        return refreshTokenDB;
    }
}
