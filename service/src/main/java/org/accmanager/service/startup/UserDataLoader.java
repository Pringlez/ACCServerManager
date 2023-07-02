package org.accmanager.service.startup;

import org.accmanager.service.entity.RolesEntity;
import org.accmanager.service.entity.UsersAuthorityEntity;
import org.accmanager.service.entity.UsersEntity;
import org.accmanager.service.repository.UsersAuthorityRepository;
import org.accmanager.service.repository.UsersRepository;
import org.accmanager.service.repository.UsersRolesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserDataLoader implements CommandLineRunner {

    private final UsersAuthorityRepository usersAuthorityRepository;
    private final UsersRolesRepository usersRolesRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataLoader(UsersAuthorityRepository usersAuthorityRepository, UsersRolesRepository usersRolesRepository, UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersAuthorityRepository = usersAuthorityRepository;
        this.usersRolesRepository = usersRolesRepository;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private void loadSecurityData() {
        UsersAuthorityEntity readInstance = usersAuthorityRepository.save(UsersAuthorityEntity.builder().setPermission("read.instance"));
        UsersAuthorityEntity readEntries = usersAuthorityRepository.save(UsersAuthorityEntity.builder().setPermission("read.entries"));
        UsersAuthorityEntity readEvents = usersAuthorityRepository.save(UsersAuthorityEntity.builder().setPermission("read.events"));
        UsersAuthorityEntity readEventRules = usersAuthorityRepository.save(UsersAuthorityEntity.builder().setPermission("read.eventRules"));
        UsersAuthorityEntity readBop = usersAuthorityRepository.save(UsersAuthorityEntity.builder().setPermission("read.bop"));
        UsersAuthorityEntity readAssistRules = usersAuthorityRepository.save(UsersAuthorityEntity.builder().setPermission("read.assistRules"));

        UsersAuthorityEntity writeInstance = usersAuthorityRepository.save(UsersAuthorityEntity.builder().setPermission("write.instance"));
        UsersAuthorityEntity writeEntries = usersAuthorityRepository.save(UsersAuthorityEntity.builder().setPermission("write.entries"));
        UsersAuthorityEntity writeEvents = usersAuthorityRepository.save(UsersAuthorityEntity.builder().setPermission("write.events"));
        UsersAuthorityEntity writeEventRules = usersAuthorityRepository.save(UsersAuthorityEntity.builder().setPermission("write.eventRules"));
        UsersAuthorityEntity writeBop = usersAuthorityRepository.save(UsersAuthorityEntity.builder().setPermission("write.bop"));
        UsersAuthorityEntity writeAssistRules = usersAuthorityRepository.save(UsersAuthorityEntity.builder().setPermission("write.assistRules"));

        RolesEntity adminRole = usersRolesRepository.save(RolesEntity.builder().setRoleName("ADMIN"));
        RolesEntity userRole = usersRolesRepository.save(RolesEntity.builder().setRoleName("USER"));

        adminRole.setAuthorities(Set.of(readInstance, readEntries, readEvents, readEventRules, readBop, readAssistRules, writeInstance, writeEntries,
                writeEvents, writeEventRules, writeBop, writeAssistRules));

        userRole.setAuthorities(Set.of(readInstance, readEntries, readEvents, readEventRules, readBop, readAssistRules));

        usersRolesRepository.saveAll(Arrays.asList(adminRole, userRole));

        usersRepository.save(UsersEntity.builder()
                .setUsername("user-1")
                .setPassword(passwordEncoder.encode("vxUdzhqrwt8eqQS7yszq"))
                .setRoles(new HashSet<>(List.of(adminRole)))
                .setEnabled(true)
                .setAccountNonExpired(true)
                .setCredentialsNonExpired(true)
                .setAccountNonLocked(true)
                .setCredentialUpdated(Instant.now()));

        usersRepository.save(UsersEntity.builder()
                .setUsername("user-2")
                .setPassword(passwordEncoder.encode("wKQWuDzpCQ8cxeeDzktK"))
                .setRoles(new HashSet<>(List.of(userRole)))
                .setEnabled(true)
                .setAccountNonExpired(true)
                .setCredentialsNonExpired(true)
                .setAccountNonLocked(true)
                .setCredentialUpdated(Instant.now()));

        usersRepository.save(UsersEntity.builder()
                .setUsername("user-3")
                .setPassword(passwordEncoder.encode("RTFWajHjjRBbD58PKk9h"))
                .setRoles(new HashSet<>(List.of(adminRole)))
                .setEnabled(false)
                .setAccountNonExpired(true)
                .setCredentialsNonExpired(true)
                .setAccountNonLocked(true)
                .setCredentialUpdated(Instant.now()));
    }

    @Override
    public void run(String... args) {
        if (usersAuthorityRepository.count() == 0) {
            //loadSecurityData();
        }
    }
}
