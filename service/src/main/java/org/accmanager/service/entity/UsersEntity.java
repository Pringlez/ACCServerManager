package org.accmanager.service.entity;

import jakarta.persistence.*;

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
    private Integer userId;

    @Basic
    @Column(name = "USERNAME")
    private String username;

    @Basic
    @Column(name = "PASSWORD")
    private String password;

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

    public Integer getUserId() {
        return userId;
    }

    public UsersEntity setUserId(Integer userId) {
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
