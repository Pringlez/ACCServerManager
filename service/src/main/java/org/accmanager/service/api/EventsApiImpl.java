package org.accmanager.service.api;

import org.accmanager.api.EventsApi;
import org.accmanager.model.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class EventsApiImpl implements EventsApi {

    @Override
    public ResponseEntity<Event> getInstanceEventById(String instanceId, String eventId) {
        return null;
    }

    @Override
    public ResponseEntity<Event> createInstanceEventById(String instanceId, String eventId, Event event) {
        return null;
    }

    @Override
    public ResponseEntity<Event> updateInstanceEventById(String instanceId, String eventId, Event event) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteInstanceEventById(String instanceId, String eventId) {
        return null;
    }
}
