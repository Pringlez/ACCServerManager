package org.accmanager.service.repository.auth;

import org.accmanager.service.entity.auth.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity, String> {

    Optional<UsersEntity> findByUsername(String username);
}
