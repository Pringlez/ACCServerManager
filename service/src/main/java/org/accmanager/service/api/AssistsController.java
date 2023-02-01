package org.accmanager.service.api;

import org.accmanager.api.AssistRulesApi;
import org.accmanager.model.AssistRules;
import org.accmanager.service.services.files.FileReadWriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import static org.accmanager.service.enums.FilesEnum.ASSIST_RULES_JSON;

@Controller
public class AssistsController implements AssistRulesApi {

    private final FileReadWriteService fileReadWriteService;

    public AssistsController(FileReadWriteService fileReadWriteService) {
        this.fileReadWriteService = fileReadWriteService;
    }

    @Override
    public ResponseEntity<AssistRules> getAssistRulesByInstanceId(String instanceId, String assistsId) {
        return new ResponseEntity<>((AssistRules) fileReadWriteService.readJsonFile(
                instanceId, ASSIST_RULES_JSON, AssistRules.class).orElse(new AssistRules()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AssistRules> createAssistRulesByInstanceId(String instanceId, AssistRules assistRules, String assistsId) {
        fileReadWriteService.writeJsonFile(instanceId, ASSIST_RULES_JSON, assistRules);
        return new ResponseEntity<>(assistRules, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AssistRules> updateAssistRulesByInstanceId(String instanceId, AssistRules assistRules, String assistsId) {
        fileReadWriteService.writeJsonFile(instanceId, ASSIST_RULES_JSON, assistRules);
        return new ResponseEntity<>(assistRules, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteAssistRulesByInstanceId(String instanceId, String assistsId) {
        return null;
    }
}
