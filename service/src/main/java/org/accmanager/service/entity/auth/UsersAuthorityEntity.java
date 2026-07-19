package org.accmanager.service.entity.auth;

import jakarta.persistence.*;

import java.util.Set;

import static jakarta.persistence.GenerationType.UUID;

@Entity
@Table(name = "USERS_AUTHORITY")
public class UsersAuthorityEntity {

    @Id
    @GeneratedValue(strategy = UUID)
    @Column(name = "AUTHORITY_ID")
    private String authorityId;

    @Basic
    @Column(name = "PERMISSION")
    private String permission;

    @ManyToMany(mappedBy = "authorities")
    private Set<RolesEntity> roles;

    public static UsersAuthorityEntity builder() {
        return new UsersAuthorityEntity();
    }

    public String getAuthorityId() {
        return authorityId;
    }

    public UsersAuthorityEntity setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
        return this;
    }

    public String getPermission() {
        return permission;
    }

    public UsersAuthorityEntity setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public Set<RolesEntity> getRoles() {
        return roles;
    }

    public UsersAuthorityEntity setRoles(Set<RolesEntity> roles) {
        this.roles = roles;
        return this;
    }
}
