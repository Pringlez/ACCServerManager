package org.accmanager.service.api;

import org.accmanager.api.EventsApi;
import org.accmanager.model.Event;
import org.accmanager.service.services.files.FileReadWriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import static org.accmanager.service.enums.FilesEnum.EVENT_JSON;

@Controller
public class EventsApiImpl implements EventsApi {

    private final FileReadWriteService fileReadWriteService;

    public EventsApiImpl(FileReadWriteService fileReadWriteService) {
        this.fileReadWriteService = fileReadWriteService;
    }

    @Override
    public ResponseEntity<Event> getInstanceEventById(String instanceId, String eventId) {
        return new ResponseEntity<>((Event) fileReadWriteService.readJsonFile(
                instanceId, EVENT_JSON, Event.class).orElse(new Event()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Event> createInstanceEventById(String instanceId, String eventId, Event event) {
        fileReadWriteService.writeJsonFile(instanceId, EVENT_JSON, event);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Event> updateInstanceEventById(String instanceId, String eventId, Event event) {
        fileReadWriteService.writeJsonFile(instanceId, EVENT_JSON, event);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteInstanceEventById(String instanceId, String eventId) {
        return null;
    }
}
