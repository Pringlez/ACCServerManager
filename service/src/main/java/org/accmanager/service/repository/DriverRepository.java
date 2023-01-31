package org.accmanager.service.repository;

import org.accmanager.service.entity.DriverEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends CrudRepository<DriverEntity, String> {

    Optional<List<DriverEntity>> findDriverEntitiesByCarEntryId(String carEntryId);
}
