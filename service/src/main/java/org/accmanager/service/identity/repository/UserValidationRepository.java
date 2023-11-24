package org.accmanager.service.identity.repository;

import org.accmanager.service.identity.entity.UsersValidationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public
interface UserValidationRepository extends JpaRepository<UsersValidationEntity, Long> {
    Optional<UsersValidationEntity> findByToken(String token);
}
