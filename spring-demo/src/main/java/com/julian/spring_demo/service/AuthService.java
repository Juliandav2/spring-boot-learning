package com.julian.spring_demo.service;

import com.julian.spring_demo.dto.AuthResponseDTO;
import com.julian.spring_demo.dto.LoginRequestDTO;
import com.julian.spring_demo.dto.RegisterRequestDTO;
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

    public AuthService (UserRepository userRepository,
                        JwtService jwtService,
                        PasswordEncoder passwordEncoder,
                        AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
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
        String token = jwtService.generateToken(user.getEmail());
        log.info("User registered successfully: {}", user.getEmail());

        return new AuthResponseDTO(token, user.getEmail(), user.getRole().name());
    }

    @Transactional (readOnly = true)
    public AuthResponseDTO login (LoginRequestDTO dto) {
        log.info("Login attempt for email: {}", dto.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = jwtService.generateToken(user.getEmail());
        log.info("Login successful for email: {}", dto.getEmail());

        return new AuthResponseDTO(token, user.getEmail(), user.getRole().name());
    }
}
