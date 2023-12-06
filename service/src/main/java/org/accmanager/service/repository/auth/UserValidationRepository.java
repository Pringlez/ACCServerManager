package org.accmanager.service.repository.auth;

import org.accmanager.service.entity.auth.UsersValidationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserValidationRepository extends JpaRepository<UsersValidationEntity, String> {

    Optional<UsersValidationEntity> findByToken(String token);
}
