package org.accmanager.service.repository;

import org.accmanager.service.entity.BopEntryEntity;
import org.springframework.data.repository.CrudRepository;

public interface BopEntryRepository extends CrudRepository<BopEntryEntity, String> {
}
