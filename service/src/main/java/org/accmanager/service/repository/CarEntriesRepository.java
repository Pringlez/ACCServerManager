package org.accmanager.service.repository;

import org.accmanager.service.entity.CarEntriesEntity;
import org.springframework.data.repository.CrudRepository;

public interface CarEntriesRepository extends CrudRepository<CarEntriesEntity, String> {
}
