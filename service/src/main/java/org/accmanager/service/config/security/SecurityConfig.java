package org.accmanager.service.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // Auto configure password encoders
        //return new BCryptPasswordEncoder();
        //return new StandardPasswordEncoder(); // SHA-256
        //return new LdapShaPasswordEncoder(); // SSHA
    }

    /**
     * In-memory example of users & passwords with different hashing algorithms
     *
     * @param authenticationManagerBuilder
     * @throws Exception
     */
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
                .roles("USER");
    }
}
