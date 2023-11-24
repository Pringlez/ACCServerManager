package org.accmanager.service.identity.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.AUTO;

@Entity
@Table(name = "USERS")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "USER_ID")
    private Long userId;

    @Basic
    @Column(name = "USERNAME")
    private String username;

    @Basic
    @Column(name = "PASSWORD")
    private String password;

    @Basic
    @Column(name = "IS_TEST_USER")
    private boolean isTestUser;

    @Basic
    @Column(name = "TOKEN_VALIDATION")
    private Instant tokenValidation;

    @Basic
    @Column(name = "USER_CREATION")
    private Instant userCreation;

    public UsersEntity() {
    }

    public UsersEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UsersEntity(String username, String password, boolean isTestUser) {
        this.username = username;
        this.password = password;
        this.isTestUser = isTestUser;
    }

    public UsersEntity(boolean isTestUser) {
        this.isTestUser = isTestUser;
    }

    @ManyToMany(cascade = MERGE, fetch = EAGER)
    @JoinTable(name = "USERS_ROLES",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")})
    private Set<RolesEntity> roles;

    @Transient
    private Set<UsersAuthorityEntity> authorities;

    @Basic
    @Column(name = "ENABLED")
    private Boolean enabled;

    @Basic
    @Column(name = "ACCOUNT_NON_EXPIRED")
    private Boolean accountNonExpired;

    @Basic
    @Column(name = "CREDENTIALS_NON_EXPIRED")
    private Boolean credentialsNonExpired;

    @Basic
    @Column(name = "ACCOUNT_NON_LOCKED")
    private Boolean accountNonLocked;

    @Basic
    @Column(name = "CREDENTIAL_UPDATED")
    private Instant credentialUpdated;

    public static UsersEntity builder() {
        return new UsersEntity();
    }

    public Long getUserId() {
        return userId;
    }

    public UsersEntity setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UsersEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UsersEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean isTestUser() {
        return isTestUser;
    }

    public UsersEntity setTestUser(boolean testUser) {
        isTestUser = testUser;
        return this;
    }

    public Instant getTokenValidation() {
        return tokenValidation;
    }

    public UsersEntity setTokenValidation(Instant tokenValidation) {
        this.tokenValidation = tokenValidation;
        return this;
    }

    public void markTokenAsValid() {
        setTokenValidation(Instant.now());
    }

    public boolean validated() {
        return tokenValidation != null;
    }

    @CreationTimestamp
    public Instant getUserCreation() {
        return userCreation;
    }

    public UsersEntity setUserCreation(Instant userCreation) {
        this.userCreation = userCreation;
        return this;
    }

    public Set<RolesEntity> getRoles() {
        return roles;
    }

    public UsersEntity setRoles(Set<RolesEntity> roles) {
        this.roles = roles;
        return this;
    }

    public Set<UsersAuthorityEntity> getAuthorities() {
        return this.roles.stream()
                .map(RolesEntity::getAuthorities)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    public UsersEntity setAuthorities(Set<UsersAuthorityEntity> authorities) {
        this.authorities = authorities;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public UsersEntity setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Boolean getAccountNonExpired() {
        return accountNonExpired;
    }

    public UsersEntity setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
        return this;
    }

    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public UsersEntity setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
        return this;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public UsersEntity setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
        return this;
    }

    public Instant getCredentialUpdated() {
        return credentialUpdated;
    }

    public UsersEntity setCredentialUpdated(Instant instantTime) {
        this.credentialUpdated = instantTime;
        return this;
    }
}
