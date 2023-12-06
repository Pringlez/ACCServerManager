package org.accmanager.service.repository.auth;

import org.accmanager.service.entity.auth.UsersAuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersAuthorityRepository extends JpaRepository<UsersAuthorityEntity, String> {
}
