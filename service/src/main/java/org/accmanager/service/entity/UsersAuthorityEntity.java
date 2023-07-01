package org.accmanager.service.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "USERS_AUTHORITY")
public class UsersAuthorityEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "AUTHORITY_ID")
    private Integer authorityId;

    @Basic
    @Column(name = "USER_ROLE")
    private String userRole;

    @ManyToMany(mappedBy = "authorities")
    private Set<UsersEntity> users;

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

    public String getUserRole() {
        return userRole;
    }

    public UsersAuthorityEntity setUserRole(String userRole) {
        this.userRole = userRole;
        return this;
    }

    public Set<UsersEntity> getUsers() {
        return users;
    }

    public UsersAuthorityEntity setUsers(Set<UsersEntity> users) {
        this.users = users;
        return this;
    }
}
