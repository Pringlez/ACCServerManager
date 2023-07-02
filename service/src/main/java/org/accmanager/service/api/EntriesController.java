package org.accmanager.service.api;

import org.accmanager.api.EntriesApi;
import org.accmanager.model.EntriesList;
import org.accmanager.service.services.files.FileReadWriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import static org.accmanager.service.enums.FilesEnum.ENTRY_LIST_JSON;

@Controller
public class EntriesController implements EntriesApi {

    private final FileReadWriteService fileReadWriteService;

    public EntriesController(FileReadWriteService fileReadWriteService) {
        this.fileReadWriteService = fileReadWriteService;
    }

    @PreAuthorize("hasAuthority('write.entries')")
    @Override
    public ResponseEntity<EntriesList> getEntriesByInstanceId(String instanceId, String entriesId) {
        return new ResponseEntity<>((EntriesList) fileReadWriteService.readJsonFile(
                instanceId, ENTRY_LIST_JSON, EntriesList.class).orElse(new EntriesList()), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('write.entries')")
    @Override
    public ResponseEntity<EntriesList> createEntriesByInstanceId(String instanceId, EntriesList entriesList, String entriesId) {
        fileReadWriteService.writeJsonFile(instanceId, ENTRY_LIST_JSON, entriesList);
        return new ResponseEntity<>(entriesList, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('write.entries')")
    @Override
    public ResponseEntity<EntriesList> updateEntriesByInstanceId(String instanceId, EntriesList entriesList, String entriesId) {
        fileReadWriteService.writeJsonFile(instanceId, ENTRY_LIST_JSON, entriesList);
        return new ResponseEntity<>(entriesList, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('write.entries')")
    @Override
    public ResponseEntity<Void> deleteEntriesByInstanceId(String instanceId, String entriesId) {
        return null;
    }
}
