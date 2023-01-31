package org.accmanager.service.repository;

import org.accmanager.service.entity.BopEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BopRepository extends CrudRepository<BopEntity, String> {

    Optional<BopEntity> findBopEntityByBopId(String bopId);
}
