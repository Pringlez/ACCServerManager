package org.accmanager.service.api;

import org.accmanager.api.BopApi;
import org.accmanager.model.BoP;
import org.accmanager.service.services.files.FileReadWriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import static org.accmanager.service.enums.FilesEnum.BOP_JSON;

@Controller
public class BoPApiImpl implements BopApi {

    private final FileReadWriteService fileReadWriteService;

    public BoPApiImpl(FileReadWriteService fileReadWriteService) {
        this.fileReadWriteService = fileReadWriteService;
    }

    @Override
    public ResponseEntity<BoP> getBoPByInstanceId(String instanceId, String bopId) {
        return new ResponseEntity<>((BoP) fileReadWriteService.readJsonFile(
                instanceId, BOP_JSON, BoP.class).orElse(new BoP()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BoP> createBoPByInstanceId(String instanceId, BoP boP, String bopId) {
        fileReadWriteService.writeJsonFile(instanceId, BOP_JSON, boP);
        return new ResponseEntity<>(boP, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BoP> updateBoPByInstanceId(String instanceId, BoP boP, String bopId) {
        fileReadWriteService.writeJsonFile(instanceId, BOP_JSON, boP);
        return new ResponseEntity<>(boP, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteBoPByInstanceId(String instanceId, String bopId) {
        return null;
    }
}
