package org.accmanager.service.identity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "USERS_VALIDATION")
public class UsersValidationEntity {

    @Id
    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "PASSWORD_RESET_TOKEN")
    private String passwordResetToken;

    @Column(name = "PASSWORD_RESET_ISSUED")
    private Instant passwordResetIssued;

    @Column(name = "CREATION")
    @CreationTimestamp
    private Instant creation;

    private String token;
    private Instant tokenIssue;

    public UsersValidationEntity() {
    }

    public UsersValidationEntity(UsersEntity userId) {
        this.userId = userId.getUserId();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public Instant getPasswordResetIssued() {
        return passwordResetIssued;
    }

    public void setPasswordResetIssued(Instant passwordResetIssued) {
        this.passwordResetIssued = passwordResetIssued;
    }

    public void newPasswordResetToken() {
        setPasswordResetToken(UUID.randomUUID().toString());
        setPasswordResetIssued(Instant.now());
    }

    public Instant getCreation() {
        return creation;
    }

    public void setCreation(Instant creation) {
        this.creation = creation;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void newToken() {
        setToken(UUID.randomUUID().toString());
        setTokenIssue(Instant.now());
    }

    public Instant getTokenIssue() {
        return tokenIssue;
    }

    public void setTokenIssue(Instant tokenIssue) {
        this.tokenIssue = tokenIssue;
    }

    public boolean tokenIsCurrent() {
        return Math.abs(Duration.between(getTokenIssue(), Instant.now()).toMillis()) < 1000 * 60 * 60 * 24;
    }

    public boolean passwordValidationIsCurrent() {
        return Math.abs(Duration.between(getPasswordResetIssued(), Instant.now()).toMillis()) < 1000 * 60 * 5;
    }
}
