package org.accmanager.service.api;

import org.accmanager.api.AuthApi;
import org.accmanager.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class AuthApiImpl implements AuthApi {

    @Override
    public ResponseEntity<String> authenticate(User user) {
        return null;
    }
}
