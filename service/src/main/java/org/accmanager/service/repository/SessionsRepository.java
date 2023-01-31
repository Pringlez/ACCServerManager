package org.accmanager.service.repository;

import org.accmanager.service.entity.SessionsEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SessionsRepository extends CrudRepository<SessionsEntity, String> {

    Optional<List<SessionsEntity>> findSessionsEntitiesByEventId(String eventId);
}
