package org.accmanager.service.repository;

import org.accmanager.service.entity.ConfigEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConfigRepository extends CrudRepository<ConfigEntity, String> {

    Optional<ConfigEntity> findConfigEntityByConfigId(String configId);
}
