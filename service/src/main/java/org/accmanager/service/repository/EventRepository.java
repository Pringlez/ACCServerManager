package org.accmanager.service.repository;

import org.accmanager.service.entity.EventEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EventRepository extends CrudRepository<EventEntity, String> {

    Optional<EventEntity> findEventEntityByEventId(String eventId);
}
