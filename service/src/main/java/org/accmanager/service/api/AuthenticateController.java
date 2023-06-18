package org.accmanager.service.api;

import org.accmanager.api.AuthenticateApi;
import org.accmanager.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class AuthenticateController implements AuthenticateApi {

    @Override
    public ResponseEntity<String> loginUser(User user) {
        return null;
    }
}
