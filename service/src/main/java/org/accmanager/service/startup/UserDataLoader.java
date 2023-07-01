package org.accmanager.service.startup;

import org.accmanager.service.entity.UsersAuthorityEntity;
import org.accmanager.service.entity.UsersEntity;
import org.accmanager.service.repository.UsersAuthorityRepository;
import org.accmanager.service.repository.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserDataLoader implements CommandLineRunner {

    private final UsersAuthorityRepository usersAuthorityRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataLoader(UsersAuthorityRepository usersAuthorityRepository, UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersAuthorityRepository = usersAuthorityRepository;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private void loadSecurityData() {
        UsersAuthorityEntity admin = usersAuthorityRepository.save(UsersAuthorityEntity.builder().setUserRole("ROLE_ADMIN"));
        UsersAuthorityEntity userRole = usersAuthorityRepository.save(UsersAuthorityEntity.builder().setUserRole("ROLE_USER"));
        UsersAuthorityEntity customer = usersAuthorityRepository.save(UsersAuthorityEntity.builder().setUserRole("ROLE_CUSTOMER"));

        Set<UsersAuthorityEntity> authoritiesSet = new HashSet<>(Arrays.asList(admin, userRole, customer));

        usersRepository.save(UsersEntity.builder()
                .setUsername("user-1")
                .setPassword(passwordEncoder.encode("vxUdzhqrwt8eqQS7yszq"))
                .setAuthorities(authoritiesSet)
                .setEnabled(true)
                .setAccountNonExpired(true)
                .setCredentialsNonExpired(true)
                .setAccountNonLocked(true)
                .setCredentialUpdated(Instant.now()));

        usersRepository.save(UsersEntity.builder()
                .setUsername("user-2")
                .setPassword(passwordEncoder.encode("wKQWuDzpCQ8cxeeDzktK"))
                .setAuthorities(authoritiesSet)
                .setEnabled(true)
                .setAccountNonExpired(true)
                .setCredentialsNonExpired(true)
                .setAccountNonLocked(false)
                .setCredentialUpdated(Instant.now()));

        usersRepository.save(UsersEntity.builder()
                .setUsername("user-3")
                .setPassword(passwordEncoder.encode("RTFWajHjjRBbD58PKk9h"))
                .setAuthorities(authoritiesSet)
                .setEnabled(false)
                .setAccountNonExpired(true)
                .setCredentialsNonExpired(true)
                .setAccountNonLocked(true)
                .setCredentialUpdated(Instant.now()));
    }

    @Override
    public void run(String... args) {
        if (usersAuthorityRepository.count() == 0) {
            loadSecurityData();
        }
    }
}
