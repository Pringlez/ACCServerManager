package org.accmanager.service.repository;

import org.accmanager.service.entity.CarEntriesEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CarEntriesRepository extends CrudRepository<CarEntriesEntity, String> {

    Optional<CarEntriesEntity> findCarEntriesEntityByCarEntriesId(String carEntriesId);
}
