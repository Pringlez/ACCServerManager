package org.accmanager.service.repository;

import org.accmanager.service.entity.AuthEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthRepository extends CrudRepository<AuthEntity, String> {

    Optional<AuthEntity> findByUsernameAndPassword(String username, String password);
    Optional<AuthEntity> findByUsername(String username);
}
