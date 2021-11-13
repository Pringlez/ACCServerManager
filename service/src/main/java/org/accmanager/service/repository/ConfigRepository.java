package org.accmanager.service.repository;

import org.accmanager.service.entity.ConfigEntity;
import org.springframework.data.repository.CrudRepository;

public interface ConfigRepository extends CrudRepository<ConfigEntity, String> {
}
