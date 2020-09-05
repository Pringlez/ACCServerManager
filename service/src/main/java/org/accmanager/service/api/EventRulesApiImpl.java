package org.accmanager.service.api;

import org.accmanager.api.EventRulesApi;
import org.accmanager.model.EventRules;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class EventRulesApiImpl implements EventRulesApi {

    @Override
    public ResponseEntity<EventRules> getInstanceEventRulesById(String instanceId, String eventRulesId) {
        return null;
    }

    @Override
    public ResponseEntity<EventRules> createInstanceEventRulesById(String instanceId, String eventRulesId, EventRules eventRules) {
        return null;
    }

    @Override
    public ResponseEntity<EventRules> updateInstanceEventRulesById(String instanceId, String eventRulesId, EventRules eventRules) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteInstanceEventRulesById(String instanceId, String eventRulesId) {
        return null;
    }
}
