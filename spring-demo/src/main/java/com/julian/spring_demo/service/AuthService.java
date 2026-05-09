package com.julian.spring_demo.service;

import com.julian.spring_demo.dto.AuthResponseDTO;
import com.julian.spring_demo.dto.LoginRequestDTO;
import com.julian.spring_demo.dto.RefreshRequestDTO;
import com.julian.spring_demo.dto.RegisterRequestDTO;
import com.julian.spring_demo.model.RefreshToken;
import com.julian.spring_demo.model.Role;
import com.julian.spring_demo.model.User;
import com.julian.spring_demo.repository.UserRepository;
import com.julian.spring_demo.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public AuthService (UserRepository userRepository,
                        JwtService jwtService,
                        PasswordEncoder passwordEncoder,
                        AuthenticationManager authenticationManager,
                        RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public AuthResponseDTO register (RegisterRequestDTO dto) {
        log.info("Registering user with email: {}", dto.getEmail());

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + dto.getEmail());
        }

        User user = new User(
                dto.getName(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                Role.USER
        );

        userRepository.save(user);
        String accessToken = jwtService.generateAccessToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        log.info("User registered successfully: {}", user.getEmail());
        return new AuthResponseDTO(accessToken, refreshToken.getToken(), user.getEmail(), user.getRole().name());
    }

    @Transactional
    public AuthResponseDTO login (LoginRequestDTO dto) {
        log.info("Login attempt for email: {}", dto.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        log.info("Login successful for email: {}", dto.getEmail());
        return new AuthResponseDTO(accessToken, refreshToken.getToken(), user.getEmail(), user.getRole().name());
    }

    @Transactional
    public AuthResponseDTO refresh (RefreshRequestDTO dto) {
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(dto.getRefreshToken());
        User user = refreshToken.getUser();
        String newAccessToken = jwtService.generateAccessToken(user.getEmail());

        log.info("Access token refreshed for email: {}", user.getEmail());
        return new AuthResponseDTO(newAccessToken, refreshToken.getToken(), user.getEmail(), user.getRole().name());
    }

    @Transactional
    public void logout (RefreshRequestDTO dto) {
        refreshTokenService.revokeRefreshToken(dto.getRefreshToken());
        log.info("User logged out successfully");
    }
}
