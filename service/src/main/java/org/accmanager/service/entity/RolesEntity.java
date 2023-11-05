package org.accmanager.service.entity;

import jakarta.persistence.*;

import java.util.Set;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.AUTO;

@Entity
@Table(name = "ROLES")
public class RolesEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "ROLE_ID")
    private Integer roleId;

    @Basic
    @Column(name = "ROLE_NAME")
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private Set<UsersEntity> users;

    @ManyToMany(cascade = MERGE, fetch = EAGER)
    @JoinTable(name = "USERS_ROLES_AUTHORITIES",
            joinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "AUTHORITY_ID")})
    private Set<UsersAuthorityEntity> authorities;

    public static RolesEntity builder() {
        return new RolesEntity();
    }

    public Integer getRoleId() {
        return roleId;
    }

    public RolesEntity setRoleId(Integer roleId) {
        this.roleId = roleId;
        return this;
    }

    public String getRoleName() {
        return roleName;
    }

    public RolesEntity setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public Set<UsersEntity> getUsers() {
        return users;
    }

    public RolesEntity setUsers(Set<UsersEntity> users) {
        this.users = users;
        return this;
    }

    public Set<UsersAuthorityEntity> getAuthorities() {
        return authorities;
    }

    public RolesEntity setAuthorities(Set<UsersAuthorityEntity> authorities) {
        this.authorities = authorities;
        return this;
    }
}
