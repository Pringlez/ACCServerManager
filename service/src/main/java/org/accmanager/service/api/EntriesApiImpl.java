package org.accmanager.service.api;

import org.accmanager.api.EntriesApi;
import org.accmanager.model.EntriesList;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class EntriesApiImpl implements EntriesApi {

    @Override
    public ResponseEntity<EntriesList> createEntriesByInstanceId(String instanceId, EntriesList entriesList, String entriesId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteEntriesByInstanceId(String instanceId, String entriesId) {
        return null;
    }

    @Override
    public ResponseEntity<EntriesList> getEntriesByInstanceId(String instanceId, String entriesId) {
        return null;
    }

    @Override
    public ResponseEntity<EntriesList> updateEntriesByInstanceId(String instanceId, EntriesList entriesList, String entriesId) {
        return null;
    }
}
