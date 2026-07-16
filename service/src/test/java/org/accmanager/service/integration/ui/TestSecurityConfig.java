package org.accmanager.service.integration.ui;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// This configuration is disabled to prevent it from polluting the Spring context in other UI tests.
// @org.springframework.boot.test.context.TestConfiguration
public class TestSecurityConfig {

    // @org.springframework.context.annotation.Bean
    // @org.springframework.context.annotation.Primary
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/web/**", "/api/v1/**", "/**.html")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/web/**").permitAll()
                        .requestMatchers("/api/v1/**").authenticated())
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable());
        return http.build();
    }
}
