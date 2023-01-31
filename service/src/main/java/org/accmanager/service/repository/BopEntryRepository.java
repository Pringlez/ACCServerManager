package org.accmanager.service.repository;

import org.accmanager.service.entity.BopEntryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BopEntryRepository extends CrudRepository<BopEntryEntity, String> {

    Optional<List<BopEntryEntity>> findBopEntryEntitiesByBopEntryId(String bopEntryId);
}
