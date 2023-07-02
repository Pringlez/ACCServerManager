package org.accmanager.service.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "USERS_AUTHORITY")
public class UsersAuthorityEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "AUTHORITY_ID")
    private Integer authorityId;

    @Basic
    @Column(name = "PERMISSION")
    private String permission;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")
    private UsersRolesEntity roles;

    public static UsersAuthorityEntity builder() {
        return new UsersAuthorityEntity();
    }

    public Integer getAuthorityId() {
        return authorityId;
    }

    public UsersAuthorityEntity setAuthorityId(Integer authorityId) {
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

    public UsersRolesEntity getRoles() {
        return roles;
    }

    public UsersAuthorityEntity setRoles(UsersRolesEntity roles) {
        this.roles = roles;
        return this;
    }
}
