package org.accmanager.service.repository;

import org.accmanager.service.entity.RolesEntity;
import org.springframework.data.repository.CrudRepository;

public interface UsersRolesRepository extends CrudRepository<RolesEntity, Integer> {

}
