package org.accmanager.service.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AUTH")
public class AuthEntity {

    private String authId;
    private String username;
    private String password;
    private String accountLocked;
    private String accountExpired;
    private String credentialExpired;
    private String credentialUpdated;

    @Id
    @Column(name = "INSTANCE_ID")
    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    @Basic
    @Column(name = "USERNAME")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "ACCOUNT_LOCKED")
    public String getAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(String accountLocked) {
        this.accountLocked = accountLocked;
    }

    @Basic
    @Column(name = "ACCOUNT_EXPIRED")
    public String getAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(String accountExpired) {
        this.accountExpired = accountExpired;
    }

    @Basic
    @Column(name = "CREDENTIAL_EXPIRED")
    public String getCredentialExpired() {
        return credentialExpired;
    }

    public void setCredentialExpired(String credentialExpired) {
        this.credentialExpired = credentialExpired;
    }

    @Basic
    @Column(name = "CREDENTIAL_UPDATED")
    public String getCredentialUpdated() {
        return credentialUpdated;
    }

    public void setCredentialUpdated(String credentialUpdated) {
        this.credentialUpdated = credentialUpdated;
    }
}
