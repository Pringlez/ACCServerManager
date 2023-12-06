package org.accmanager.service.repository.auth;

import org.accmanager.service.entity.auth.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRolesRepository extends JpaRepository<RolesEntity, String> {

    Optional<RolesEntity> findByRoleName(String roleName);
}
