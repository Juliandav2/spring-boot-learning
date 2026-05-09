package com.julian.spring_demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table (name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn (name = "user_id", nullable = false)
    private User user;

    @Column (nullable = false)
    private LocalDateTime expiryDate;

    @Column (nullable = false)
    private boolean revoked = false;

    public RefreshToken () {}

    public RefreshToken (String token, User user, LocalDateTime expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    public boolean isExpired () {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
}
