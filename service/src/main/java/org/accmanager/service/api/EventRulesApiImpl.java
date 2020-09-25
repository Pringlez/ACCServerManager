package org.accmanager.service.api;

import org.accmanager.api.EventRulesApi;
import org.accmanager.model.EventRules;
import org.accmanager.service.services.files.FileReadWriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import static org.accmanager.service.enums.FilesEnum.EVENT_RULES_JSON;

@Controller
public class EventRulesApiImpl implements EventRulesApi {

    private final FileReadWriteService fileReadWriteService;

    public EventRulesApiImpl(FileReadWriteService fileReadWriteService) {
        this.fileReadWriteService = fileReadWriteService;
    }

    @Override
    public ResponseEntity<EventRules> getInstanceEventRulesById(String instanceId, String eventRulesId) {
        return new ResponseEntity<>((EventRules) fileReadWriteService.readJsonFile(
                instanceId, EVENT_RULES_JSON, EventRules.class).orElse(new EventRules()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EventRules> createInstanceEventRulesById(String instanceId, String eventRulesId, EventRules eventRules) {
        fileReadWriteService.writeJsonFile(instanceId, EVENT_RULES_JSON, eventRules);
        return new ResponseEntity<>(eventRules, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EventRules> updateInstanceEventRulesById(String instanceId, String eventRulesId, EventRules eventRules) {
        fileReadWriteService.writeJsonFile(instanceId, EVENT_RULES_JSON, eventRules);
        return new ResponseEntity<>(eventRules, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteInstanceEventRulesById(String instanceId, String eventRulesId) {
        return null;
    }
}
