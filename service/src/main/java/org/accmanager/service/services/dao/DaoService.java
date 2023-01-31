package org.accmanager.service.services.dao;

import java.util.Optional;

public interface DaoService<T> {

    Optional<T> retrieveById(String instanceId);

    Optional<T> retrieveAll();

    void updateServerById(String id, T obj);

    void saveData(T obj);
}
