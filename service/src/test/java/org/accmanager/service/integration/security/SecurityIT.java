package org.accmanager.service.integration.security;

import org.accmanager.service.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class SecurityIT extends BaseIT {

    @Test
    public void actuatorInfoWithUser1() throws Exception {
        mockMvc.perform(get("/api/v1/instances/ae85423a-b502-4833-bcc2-a424d3f8281e/start").with(httpBasic("user-1", "9k7nEa45dPzVuG64")))
                .andExpect(status().isOk());
    }

    @Test
    public void actuatorHealthWithUser2() throws Exception {
        mockMvc.perform(get("/api/v1/instances/ae85423a-b502-4833-bcc2-a424d3f8281e/start").with(httpBasic("user-2", "9k7nEa45dPzVuG64")))
                .andExpect(status().isOk());
    }

    @Test
    public void actuatorHealthWithUser3() throws Exception {
        mockMvc.perform(get("/api/v1/instances/ae85423a-b502-4833-bcc2-a424d3f8281e/start").with(httpBasic("user-3", "9k7nEa45dPzVuG64")))
                .andExpect(status().isOk());
    }

    @Test
    public void actuatorHealthWithDefaultUser() throws Exception {
        mockMvc.perform(get("/api/v1/instances/ae85423a-b502-4833-bcc2-a424d3f8281e/start").with(httpBasic("acc-user", "9k7nEa45dPzVuG64")))
                .andExpect(status().isUnauthorized());
    }
}
