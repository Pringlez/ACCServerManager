package org.accmanager.service.api;

import org.accmanager.api.AssistsApi;
import org.accmanager.model.AssistRules;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class AssistsApiImpl implements AssistsApi {

    @Override
    public ResponseEntity<AssistRules> getAssistRulesByInstanceId(String instanceId) {
        return null;
    }

    @Override
    public ResponseEntity<AssistRules> createAssistRulesByInstanceId(String instanceId, AssistRules assistRules) {
        return null;
    }

    @Override
    public ResponseEntity<AssistRules> updateAssistRulesByInstanceId(String instanceId, AssistRules assistRules) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteAssistRulesByInstanceId(String instanceId) {
        return null;
    }
}
