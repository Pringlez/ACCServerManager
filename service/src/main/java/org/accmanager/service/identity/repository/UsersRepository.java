package org.accmanager.service.identity.repository;

import org.accmanager.service.identity.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity, String> {

    Optional<UsersEntity> findByUsername(String username);
}
