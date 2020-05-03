package org.accmanager.service.api;

import org.accmanager.api.BopApi;
import org.accmanager.model.BoP;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class BoPApiImpl implements BopApi {

    @Override
    public ResponseEntity<BoP> getBoPByInstanceId(String instanceId) {
        return null;
    }

    @Override
    public ResponseEntity<BoP> createBoPByInstanceId(String instanceId, BoP boP) {
        return null;
    }

    @Override
    public ResponseEntity<BoP> updateBoPByInstanceId(String instanceId, BoP boP) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteBoPByInstanceId(String instanceId) {
        return null;
    }
}
