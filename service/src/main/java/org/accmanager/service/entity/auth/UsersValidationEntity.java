package org.accmanager.service.entity.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "USERS_VALIDATION")
public class UsersValidationEntity {

    @Id
    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "PASSWORD_RESET_TOKEN")
    private String passwordResetToken;

    @Column(name = "PASSWORD_RESET_ISSUED")
    private Instant passwordResetIssued;

    @Column(name = "CREATION")
    @CreationTimestamp
    private Instant creation;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "TOKEN_ISSUED")
    private Instant tokenIssued;

    public UsersValidationEntity() {
    }

    public UsersValidationEntity(UsersEntity userId) {
        this.userId = userId.getUserId();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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
        setPasswordResetToken(java.util.UUID.randomUUID().toString());
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
        setToken(java.util.UUID.randomUUID().toString());
        setTokenIssued(Instant.now());
    }

    public Instant getTokenIssued() {
        return tokenIssued;
    }

    public void setTokenIssued(Instant tokenIssued) {
        this.tokenIssued = tokenIssued;
    }

    public boolean tokenIsCurrent() {
        return Math.abs(Duration.between(getTokenIssued(), Instant.now()).toMillis()) < 1000 * 60 * 60 * 24;
    }

    public boolean passwordValidationIsCurrent() {
        return Math.abs(Duration.between(getPasswordResetIssued(), Instant.now()).toMillis()) < 1000 * 60 * 5;
    }
}
