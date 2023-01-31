package org.accmanager.service.repository;

import org.accmanager.service.entity.AssistsEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AssistsRepository extends CrudRepository<AssistsEntity, String> {

    Optional<AssistsEntity> findAssistsEntityByAssistsId(String assistsId);
}
