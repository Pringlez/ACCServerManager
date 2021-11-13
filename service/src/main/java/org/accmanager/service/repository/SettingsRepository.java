package org.accmanager.service.repository;

import org.accmanager.service.entity.SettingsEntity;
import org.springframework.data.repository.CrudRepository;

public interface SettingsRepository extends CrudRepository<SettingsEntity, String> {
}
