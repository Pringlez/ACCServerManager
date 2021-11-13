package org.accmanager.service.repository;

import org.accmanager.service.entity.EventEntity;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<EventEntity, String> {
}
