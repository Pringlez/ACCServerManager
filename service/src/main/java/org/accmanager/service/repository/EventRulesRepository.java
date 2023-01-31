package org.accmanager.service.repository;

import org.accmanager.service.entity.EventRulesEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EventRulesRepository extends CrudRepository<EventRulesEntity, String> {
    
    Optional<EventRulesEntity> findEventRulesEntityByEventRulesId(String eventRulesId);
}
