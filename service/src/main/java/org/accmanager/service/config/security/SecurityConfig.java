package org.accmanager.service.config.security;

import org.accmanager.service.services.security.UserAuthDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserAuthDetailsService userAuthDetailsService;

    public SecurityConfig(UserAuthDetailsService userAuthDetailsService) {
        this.userAuthDetailsService = userAuthDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> {
                    authorize.antMatchers("/h2-console/**").permitAll();
                    authorize.antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll();
                }).authorizeRequests()
                .anyRequest()
                .authenticated()
                .and().formLogin()
                .and().httpBasic()
                .and().cors()
                .and().csrf().disable();
        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap();
        encoders.put(encodingId, new BCryptPasswordEncoder());
        encoders.put("ldap", new LdapShaPasswordEncoder());
        encoders.put("MD5", new MessageDigestPasswordEncoder("MD5"));
        encoders.put("sha256", new StandardPasswordEncoder());
        return new DelegatingPasswordEncoder(encodingId, encoders);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userAuthDetailsService);
    }

    // In-memory username & password setup
    /*
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.inMemoryAuthentication()
                .withUser("user-1")
                .password("{bcrypt}$2a$10$Wukd6uCItNR7TMz3jAaaseeyjc11PvUDOXfJX2nGpyfnBhMfq3Glu")
                .roles("ADMIN")
                .and()
                .withUser("user-2")
                .password("{MD5}a242b4d8f248e063894bde98e211f2ab")
                .roles("USER")
                .and()
                .withUser("user-3")
                .password("{sha256}bda2f7abb85455b6b52b88e6afdb41781f54d00c0e4aac92db040f1f23cf4b9370b6b839dfaacf65")
                .roles("USER")
                .and()
                .withUser("user-4")
                .password("{ldap}RCPvwn+C7/kK93ZBY8KjdJ5XiJH5XaCGPQz5Xg==")
                .roles("USER");
    }*/
}
