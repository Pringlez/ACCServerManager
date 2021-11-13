package org.accmanager.service.repository;

import org.accmanager.service.entity.DriverEntity;
import org.springframework.data.repository.CrudRepository;

public interface DriverRepository extends CrudRepository<DriverEntity, String> {
}
