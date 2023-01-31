package org.accmanager.service.repository;

import org.accmanager.service.entity.InstancesEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface InstancesRepository extends CrudRepository<InstancesEntity, String> {

    Optional<InstancesEntity> findInstancesEntityByInstanceId(String instanceId);
}
