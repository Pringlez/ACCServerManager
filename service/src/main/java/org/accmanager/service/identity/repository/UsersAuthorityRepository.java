package org.accmanager.service.identity.repository;

import org.accmanager.service.identity.entity.UsersAuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersAuthorityRepository extends JpaRepository<UsersAuthorityEntity, String> {
}
