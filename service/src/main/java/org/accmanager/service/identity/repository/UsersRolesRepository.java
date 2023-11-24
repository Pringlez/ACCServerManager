package org.accmanager.service.identity.repository;

import org.accmanager.service.identity.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRolesRepository extends JpaRepository<RolesEntity, Long> {

}
