package org.accmanager.service.repository;

import org.accmanager.service.entity.SettingsEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SettingsRepository extends CrudRepository<SettingsEntity, String> {

    Optional<SettingsEntity> findSettingsEntityBySettingsId(String settingsId);
}
