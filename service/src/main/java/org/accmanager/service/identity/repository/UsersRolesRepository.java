package org.accmanager.service.identity.repository;

import org.accmanager.service.identity.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRolesRepository extends JpaRepository<RolesEntity, String> {

    Optional<RolesEntity> findByRoleName(String roleName);
}
