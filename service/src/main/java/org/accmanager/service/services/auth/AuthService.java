package org.accmanager.service.services.auth;

import jakarta.mail.AuthenticationFailedException;
import org.accmanager.service.exception.IdentityServiceException;
import org.accmanager.service.entity.auth.RolesEntity;
import org.accmanager.service.entity.auth.UsersEntity;
import org.accmanager.service.entity.auth.UsersValidationEntity;
import org.accmanager.service.repository.auth.UserValidationRepository;
import org.accmanager.service.repository.auth.UsersRepository;
import org.accmanager.service.repository.auth.UsersRolesRepository;
import org.accmanager.service.services.email.EmailSenderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.accmanager.service.exception.IdentityServiceException.Reason.BAD_EMAIL;
import static org.accmanager.service.exception.IdentityServiceException.Reason.BAD_LOGIN;
import static org.accmanager.service.exception.IdentityServiceException.Reason.BAD_PASSWORD;
import static org.accmanager.service.exception.IdentityServiceException.Reason.BAD_PASSWORD_RESET;
import static org.accmanager.service.exception.IdentityServiceException.Reason.BAD_TOKEN;

@Service
public class AuthService {

    private final UsersRepository usersRepository;
    private final UsersRolesRepository usersRolesRepository;
    private final UserValidationRepository userValidationRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;

    @Value("${external.server.address:}")
    private String serverAddress;

    @Value("${spring.mail.username}")
    private String emailSender;

    public AuthService(UsersRepository usersRepository, UsersRolesRepository usersRolesRepository, UserValidationRepository userValidationRepository,
                       PasswordEncoder passwordEncoder, EmailSenderService emailSenderService) {
        this.usersRepository = usersRepository;
        this.usersRolesRepository = usersRolesRepository;
        this.userValidationRepository = userValidationRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSenderService = emailSenderService;
    }

    public UsersValidationEntity validation(UsersEntity user) {
        return userValidationRepository.findById(user.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("Need to save the user before using validation."));
    }

    private void sendConfirmationMail(UsersEntity user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("ACC Manager: Account Setup");
        mailMessage.setFrom(emailSender);
        mailMessage.setText("Thank you for registering!\n" + "Please click on the below link to activate your account.\n\n" +
                serverAddress + "/public/sign-up/confirm?token=" + validation(user).getToken());
        emailSenderService.sendEmail(mailMessage);
    }

    private void sendPasswordResetLink(UsersEntity user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("ACC Manager: Password Reset Link");
        mailMessage.setFrom(emailSender);
        mailMessage.setText("Here's your password reset link. Only valid for apx two hours.\n" +
                "Please click on the below link to reset your account password.\n\n" + serverAddress
                + "/public/password-reset?token=" + validation(user).getPasswordResetToken());
        emailSenderService.sendEmail(mailMessage);
    }

    private void checkEmailAddress(String address) throws IdentityServiceException {
        if (!address.contains("@")) {
            throw new IdentityServiceException(BAD_EMAIL, "Invalid email address, 800 error code");
        }
        if (address.endsWith("@")) {
            throw new IdentityServiceException(BAD_EMAIL, "Invalid email address, 801 error code");
        }
        if (address.startsWith("@")) {
            throw new IdentityServiceException(BAD_EMAIL, "Invalid email address, 802 error code");
        }
        if (address.length() < 5) {
            throw new IdentityServiceException(BAD_EMAIL, "Invalid email address, 803 error code");
        }
        if (!address.contains(".")) {
            throw new IdentityServiceException(BAD_EMAIL, "Invalid email address, 804 error code");
        }
    }

    private void checkPassword(String password) throws IdentityServiceException {
        if (password == null) {
            throw new IdentityServiceException(BAD_PASSWORD, "No password set.");
        }
        if (password.length() < 12) {
            throw new IdentityServiceException(BAD_PASSWORD, "Password is too short.");
        }
        if (password.length() > 200) {
            throw new IdentityServiceException(BAD_PASSWORD, "Password is too long.");
        }
        if (!password.trim().equals(password)) {
            throw new IdentityServiceException(BAD_PASSWORD, "No spaces in password.");
        }
        if (password.contains(" ")) {
            throw new IdentityServiceException(BAD_PASSWORD, "No spaces in password.");
        }
        String clean = password.replaceAll("[^\\n\\r\\t\\p{Print}]", "");
        if (!password.equals(clean)) {
            throw new IdentityServiceException(BAD_PASSWORD, "No non-printable characters in password.");
        }
    }

    public UsersEntity signIn(String username, String pass) throws IdentityServiceException {
        UsersEntity user = usersRepository.findByUsername(username).orElseThrow(() ->
                new IdentityServiceException(BAD_LOGIN, "Unknown email address."));

        if (!passwordEncoder.matches(pass, user.getPassword())) {
            throw new IdentityServiceException(BAD_LOGIN, "Invalid login credentials.");
        }

        return user;
    }

    public UsersEntity signUpUser(String username, String pass, boolean isTestUser) throws IdentityServiceException, AuthenticationFailedException {
        checkEmailAddress(username);
        checkPassword(pass);

        Optional<UsersEntity> foundUser = usersRepository.findByUsername(username);

        Optional<RolesEntity> userRole = usersRolesRepository.findByRoleName("USER");

        if (foundUser.isPresent()) {
            throw new IdentityServiceException(BAD_EMAIL, "Email already exists.");
        }

        UsersEntity newUser = new UsersEntity();
        newUser.setUsername(username.trim().toLowerCase());
        newUser.setPassword(passwordEncoder.encode(pass));
        newUser.setTestUser(isTestUser);
        newUser.setUserCreation(Instant.now());
        newUser.setRoles(new HashSet<>(List.of(userRole.orElse(new RolesEntity()))));
        newUser.setEnabled(true);
        newUser.setAccountNonExpired(true);
        newUser.setCredentialsNonExpired(true);
        newUser.setAccountNonLocked(true);
        newUser.setCredentialUpdated(Instant.now());

        if (!passwordEncoder.matches(pass, newUser.getPassword())) {
            throw new IllegalArgumentException("Could not match an encoded password!");
        }

        newUser = usersRepository.save(newUser);

        UsersValidationEntity usersValidation = new UsersValidationEntity(newUser);
        usersValidation.newToken();
        usersValidation = userValidationRepository.save(usersValidation);

        sendConfirmationMail(newUser);

        return newUser;
    }

    public void deleteUser(UsersEntity user) {
        if (!user.isTestUser()) {
            throw new IllegalArgumentException("Can only delete test users!");
        }

        userValidationRepository.deleteById(user.getUserId());
        usersRepository.delete(user);
    }

    private UsersEntity existingUserSignup(UsersEntity user) throws AuthenticationFailedException {
        if (user.getTokenValidation() != null) {
            return user;
        }
        if (validation(user).tokenIsCurrent()) {
            sendConfirmationMail(user);
        } else {
            validation(user).newToken();
            userValidationRepository.save(validation(user));
        }
        return user;
    }

    public Optional<UsersEntity> confirmUser(String confirmationToken) throws IdentityServiceException {
        UsersValidationEntity usersValidation = userValidationRepository.findByToken(confirmationToken).orElseThrow(() ->
                new IdentityServiceException(BAD_TOKEN, "Invalid token, 900 error code!"));

        UsersEntity user = usersRepository.findById(usersValidation.getUserId()).orElseThrow(() ->
                new IdentityServiceException(BAD_TOKEN, "Invalid token, 901 error code!"));

        if (!validation(user).tokenIsCurrent()) {
            throw new IdentityServiceException(BAD_TOKEN, "Invalid token, 902 error code!");
        }

        user.markTokenAsValid();
        UsersEntity savedUser = usersRepository.save(user);

        return Optional.of(savedUser);
    }

    public Optional<UsersEntity> findUser(String username) {
        return usersRepository.findByUsername(username);
    }

    public UsersEntity update(UsersEntity user) {
        return usersRepository.save(user);
    }

    public UsersValidationEntity update(UsersValidationEntity usersValidationEntity) {
        return userValidationRepository.save(usersValidationEntity);
    }

    public UsersValidationEntity requestPasswordReset(String username) throws IdentityServiceException, AuthenticationFailedException {
        UsersEntity user = usersRepository.findByUsername(username).orElseThrow(()
                -> new IdentityServiceException(BAD_PASSWORD_RESET, "Missing email address."));

        if (!user.validated()) {
            throw new IdentityServiceException(BAD_TOKEN, "User never activated, should resend activation email.");
        }

        UsersValidationEntity usersValidation = userValidationRepository.findById(user.getUserId()).orElseThrow(()
                -> new IdentityServiceException(BAD_PASSWORD_RESET, "No validation token found."));

        if (usersValidation.getPasswordResetIssued() != null) {
            if (usersValidation.passwordValidationIsCurrent()) {
                return usersValidation;
            }
        }

        usersValidation.newPasswordResetToken();
        usersValidation = userValidationRepository.save(usersValidation);

        sendPasswordResetLink(user);

        return usersValidation;
    }

    public UsersEntity updatePassword(String username, String passwordResetToken, String newPassword) throws IdentityServiceException {
        UsersEntity user = usersRepository.findByUsername(username).orElseThrow(() ->
                new IdentityServiceException(BAD_PASSWORD_RESET, "No user found with this email."));

        UsersValidationEntity usersValidationEntity = userValidationRepository.findById(user.getUserId()).orElseThrow(()
                -> new IdentityServiceException(BAD_PASSWORD_RESET, "No user validation token found."));

        if (!usersValidationEntity.getPasswordResetToken().equals(passwordResetToken)) {
            throw new IdentityServiceException(BAD_PASSWORD_RESET, "Invalid or expired token.");
        }
        if (!usersValidationEntity.passwordValidationIsCurrent()) {
            throw new IdentityServiceException(BAD_PASSWORD_RESET, "Validation token expired.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        usersValidationEntity.setPasswordResetIssued(null);
        usersValidationEntity.setPasswordResetToken(null);
        userValidationRepository.save(usersValidationEntity);

        return usersRepository.save(user);
    }
}
