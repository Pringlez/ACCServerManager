package org.accmanager.service.repository;

import org.accmanager.service.entity.AssistRulesEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AssistRulesRepository extends CrudRepository<AssistRulesEntity, String> {

    Optional<AssistRulesEntity> findAssistsEntityByAssistsId(String assistsId);
}
