package org.accmanager.service.services.dao;

import org.accmanager.model.Instance;

import java.util.Optional;

public interface DaoService <T> {

    Optional<T> retrieveById(String id, T obj);
    Optional<T> retrieveAll();
    void updateServerById(String id, T obj);
    void saveData(T obj);
}
