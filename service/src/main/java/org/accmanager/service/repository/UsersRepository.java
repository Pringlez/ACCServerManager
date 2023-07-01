package org.accmanager.service.repository;

import org.accmanager.service.entity.UsersEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<UsersEntity, Integer> {

    Optional<UsersEntity> findByUsername(String username);
}
