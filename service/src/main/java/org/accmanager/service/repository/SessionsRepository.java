package org.accmanager.service.repository;

import org.accmanager.service.entity.SessionsEntity;
import org.springframework.data.repository.CrudRepository;

public interface SessionsRepository extends CrudRepository<SessionsEntity, String> {
}
