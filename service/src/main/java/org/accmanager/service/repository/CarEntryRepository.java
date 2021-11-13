package org.accmanager.service.repository;

import org.accmanager.service.entity.CarEntryEntity;
import org.springframework.data.repository.CrudRepository;

public interface CarEntryRepository extends CrudRepository<CarEntryEntity, String> {
}
