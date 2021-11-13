package org.accmanager.service.repository;

import org.accmanager.service.entity.InstancesEntity;
import org.springframework.data.repository.CrudRepository;

public interface InstancesRepository extends CrudRepository<InstancesEntity, String> {
}
