package org.accmanager.service.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "USERS_ROLES")
public class UsersRolesEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "ROLE_ID")
    private Integer roleId;

    @Basic
    @Column(name = "ROLE_NAME")
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private Set<UsersEntity> users;

    @OneToMany(cascade = MERGE, fetch = EAGER, mappedBy = "roles")
    private Set<UsersAuthorityEntity> authorities;

    public static UsersRolesEntity builder() {
        return new UsersRolesEntity();
    }

    public Integer getRoleId() {
        return roleId;
    }

    public UsersRolesEntity setRoleId(Integer roleId) {
        this.roleId = roleId;
        return this;
    }

    public String getRoleName() {
        return roleName;
    }

    public UsersRolesEntity setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public Set<UsersEntity> getUsers() {
        return users;
    }

    public UsersRolesEntity setUsers(Set<UsersEntity> users) {
        this.users = users;
        return this;
    }

    public Set<UsersAuthorityEntity> getAuthorities() {
        return authorities;
    }

    public UsersRolesEntity setAuthorities(Set<UsersAuthorityEntity> authorities) {
        this.authorities = authorities;
        return this;
    }
}
