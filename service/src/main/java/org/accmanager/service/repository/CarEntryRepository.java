package org.accmanager.service.repository;

import org.accmanager.service.entity.CarEntryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CarEntryRepository extends CrudRepository<CarEntryEntity, String> {

    Optional<List<CarEntryEntity>> findCarEntryEntitiesByCarEntriesId(String carEntriesId);
}
