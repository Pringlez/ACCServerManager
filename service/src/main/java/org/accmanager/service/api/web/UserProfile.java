package org.accmanager.service.api.web;

import org.accmanager.service.entity.auth.RolesEntity;
import org.accmanager.service.entity.auth.UsersEntity;
import org.accmanager.service.repository.auth.UsersRepository;
import org.accmanager.service.repository.auth.UsersRolesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/web/profile")
@PreAuthorize("hasAuthority('write.instance') or @environment.getProperty('accserver.test') == 'true'")
public class UserProfile {

    @Value("${spring.thymeleaf.darkMode:false}")
    private boolean darkMode;

    private static final String IS_DARK_MODE = "isDarkMode";

    private final UsersRepository usersRepository;
    private final UsersRolesRepository usersRolesRepository;
    private final PasswordEncoder passwordEncoder;

    public UserProfile(UsersRepository usersRepository, UsersRolesRepository usersRolesRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.usersRolesRepository = usersRolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/details")
    public String index(Model model) {
        List<UsersEntity> users = usersRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("now", new Date());
        model.addAttribute(IS_DARK_MODE, darkMode);
        return "pages/profile/user-profile";
    }

    @PostMapping("/users/{id}/role")
    public String updateUserRole(@PathVariable("id") String userId, @RequestParam("role") String roleName, Model model) {
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        RolesEntity role = usersRolesRepository.findByRoleName(roleName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        Set<RolesEntity> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        usersRepository.save(user);

        model.addAttribute("user", user);
        model.addAttribute(IS_DARK_MODE, darkMode);
        return "pages/profile/user-profile :: user-row";
    }

    @PostMapping("/users/{id}/toggle")
    public String toggleUserStatus(@PathVariable("id") String userId, Model model) {
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setEnabled(!user.getEnabled());
        usersRepository.save(user);

        model.addAttribute("user", user);
        model.addAttribute(IS_DARK_MODE, darkMode);
        return "pages/profile/user-profile :: user-row";
    }

    @DeleteMapping(path = "/users/{id}/delete", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String deleteUser(@PathVariable("id") String userId) {
        usersRepository.deleteById(userId);
        return ""; // Swaps the row with empty content (deletes it from DOM)
    }

    @PostMapping("/users/add")
    public String addUser(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("role") String roleName,
                          Model model) {
        if (usersRepository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        RolesEntity role = usersRolesRepository.findByRoleName(roleName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        UsersEntity newUser = new UsersEntity();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setEnabled(true);
        newUser.setAccountNonExpired(true);
        newUser.setCredentialsNonExpired(true);
        newUser.setAccountNonLocked(true);
        newUser.setUserCreation(Instant.now());
        newUser.setCredentialUpdated(Instant.now());

        Set<RolesEntity> roles = new HashSet<>();
        roles.add(role);
        newUser.setRoles(roles);

        UsersEntity savedUser = usersRepository.save(newUser);

        model.addAttribute("user", savedUser);
        model.addAttribute(IS_DARK_MODE, darkMode);
        return "pages/profile/user-profile :: user-row";
    }

    @GetMapping(path = "/data", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    @PreAuthorize("permitAll()")
    public String data() {
        return "<p>hi! %s </p>".formatted(new Date().toString());
    }
}