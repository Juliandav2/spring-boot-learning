package com.julian.spring_demo.service;

import com.julian.spring_demo.model.RefreshToken;
import com.julian.spring_demo.model.User;
import com.julian.spring_demo.repository.RefreshTokenRepository;
import com.julian.spring_demo.security.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public RefreshTokenService (RefreshTokenRepository refreshTokenRepository,
                                JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public RefreshToken createRefreshToken (User user) {
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken(
                jwtService.generateRefreshTokenValue(),
                user,
                LocalDateTime.now().plusDays(7)
        );
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional (readOnly = true)
    public RefreshToken validateRefreshToken (String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (refreshToken.isRevoked()) {
            throw new IllegalArgumentException("Refresh token has revoked");
        }

        if (refreshToken.isExpired()) {
            throw new IllegalArgumentException("Refresh token has expired");
        }

        return refreshToken;
    }

    @Transactional
    public void revokeRefreshToken (String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }
}
