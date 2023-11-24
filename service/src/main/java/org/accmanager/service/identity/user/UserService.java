package org.accmanager.service.identity.user;

import jakarta.mail.AuthenticationFailedException;
import org.accmanager.service.exception.IdentityServiceException;
import org.accmanager.service.identity.entity.UsersEntity;
import org.accmanager.service.identity.entity.UsersValidationEntity;
import org.accmanager.service.identity.repository.UserValidationRepository;
import org.accmanager.service.identity.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.accmanager.service.exception.IdentityServiceException.Reason.BAD_EMAIL;
import static org.accmanager.service.exception.IdentityServiceException.Reason.BAD_LOGIN;
import static org.accmanager.service.exception.IdentityServiceException.Reason.BAD_PASSWORD;
import static org.accmanager.service.exception.IdentityServiceException.Reason.BAD_PASSWORD_RESET;
import static org.accmanager.service.exception.IdentityServiceException.Reason.BAD_TOKEN;

@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final UserValidationRepository userValidationRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;

    @Value("${external.server.address:}")
    private String serverAddress;

    @Value("${spring.mail.username}")
    private String emailSender;

    public UserService(UsersRepository usersRepository, UserValidationRepository userValidationRepository,
                       PasswordEncoder passwordEncoder, EmailSenderService emailSenderService) {
        this.usersRepository = usersRepository;
        this.userValidationRepository = userValidationRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSenderService = emailSenderService;
    }

    public UsersValidationEntity validation(UsersEntity user) {
        return userValidationRepository.findById(user.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("Need to save the user before using validation"));
    }

    private void sendConfirmationMail(UsersEntity user) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("ChangeNode: Finish Setting Up Your Account");
        mailMessage.setFrom(emailSender);
        mailMessage.setText("Thank you for registering!\n" +
                "Please click on the below link to activate your account.\n\n" +
                serverAddress + "/public/sign-up/confirm?token="
                + validation(user).getToken());

        emailSenderService.sendEmail(mailMessage);
    }

    private void sendPasswordResetLink(UsersEntity user) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("ChangeNode: Password Reset Link");
        mailMessage.setFrom(emailSender);
        mailMessage.setText("Here's your password reset link. Only valid for apx two hours.\n" +
                "Please click on the below link to reset your account password.\n\n" +
                serverAddress + "/public/password-reset?token="
                + validation(user).getPasswordResetToken());

        emailSenderService.sendEmail(mailMessage);
    }


    private void checkEmailAddress(String address) throws IdentityServiceException {
        if (!address.contains("@")) {
            throw new IdentityServiceException(BAD_EMAIL, "Invalid email address (1)");
        }
        if (address.endsWith("@")) {
            throw new IdentityServiceException(BAD_EMAIL, "Invalid email address (2)");
        }
        if (address.startsWith("@")) {
            throw new IdentityServiceException(BAD_EMAIL, "Invalid email address (3)");
        }
        if (address.length() < 5) {
            throw new IdentityServiceException(BAD_EMAIL, "Invalid email address (4)");
        }
        if (!address.contains(".")) {
            throw new IdentityServiceException(BAD_EMAIL, "Invalid email address (5)");
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
        UsersEntity user = usersRepository.findByUsername(username).orElseThrow(() -> new IdentityServiceException(BAD_LOGIN, "Unknown Email"));

        if (!passwordEncoder.matches(pass, user.getPassword())) {
            throw new IdentityServiceException(BAD_LOGIN, "Invalid Login (1)");
        }

        return user;
    }

    public UsersEntity signUpUser(String username, String pass, boolean isTestUser) throws IdentityServiceException, AuthenticationFailedException {
        checkEmailAddress(username);
        checkPassword(pass);

        Optional<UsersEntity> foundUser = usersRepository.findByUsername(username);

        if (foundUser.isPresent()) {
            throw new IdentityServiceException(BAD_EMAIL, "Email already exists.");
        }

        UsersEntity newUser = new UsersEntity();
        newUser.setUsername(username.trim().toLowerCase());
        newUser.setPassword(passwordEncoder.encode(pass));
        newUser.setTestUser(isTestUser);

        if (!passwordEncoder.matches(pass, newUser.getPassword())) {
            throw new IllegalArgumentException("The passwordEncoder just failed to match an encoded password!");
        }

        newUser = usersRepository.save(newUser);

        UsersValidationEntity usersValidation = new UsersValidationEntity(newUser);
        usersValidation.newToken();
        userValidationRepository.save(usersValidation);

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
        if (user.getTokenValidation() != null)
            return user;
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
                new IdentityServiceException(BAD_TOKEN, "Invalid Token (21)"));

        UsersEntity user = usersRepository.findById(usersValidation.getUserId()).orElseThrow(() ->
                new IdentityServiceException(BAD_TOKEN, "Invalid Token (22)"));

        if (!validation(user).tokenIsCurrent())
            throw new IdentityServiceException(BAD_TOKEN, "");

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
                -> new IdentityServiceException(BAD_PASSWORD_RESET, "Missing email address. (a)"));

        if (!user.validated()) {
            throw new IdentityServiceException(BAD_TOKEN, "User never activated (should resend activation email)");
        }

        UsersValidationEntity uv = userValidationRepository.findById(user.getUserId()).orElseThrow(()
                -> new IdentityServiceException(BAD_PASSWORD_RESET, "No validation token found. (b)"));

        if (uv.getPasswordResetIssued() != null) {
            if (uv.passwordValidationIsCurrent()) {
                return uv;
            }
        }

        uv.newPasswordResetToken();
        uv = userValidationRepository.save(uv);

        sendPasswordResetLink(user);

        return uv;
    }

    public UsersEntity updatePassword(String username, String passwordResetToken, String newPassword) throws IdentityServiceException {
        UsersEntity user = usersRepository.findByUsername(username).orElseThrow(() ->
                new IdentityServiceException(BAD_PASSWORD_RESET, "No user found with this email. (c)"));

        UsersValidationEntity usersValidationEntity = userValidationRepository.findById(user.getUserId()).orElseThrow(()
                -> new IdentityServiceException(BAD_PASSWORD_RESET, "No user validation token[s] found. (d)"));

        if (!usersValidationEntity.getPasswordResetToken().equals(passwordResetToken)) {
            throw new IdentityServiceException(BAD_PASSWORD_RESET, "Invalid/expired token. (e)");
        }
        if (!usersValidationEntity.passwordValidationIsCurrent()) {
            throw new IdentityServiceException(BAD_PASSWORD_RESET, "Token expired. (f)");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        usersValidationEntity.setPasswordResetIssued(null);
        usersValidationEntity.setPasswordResetToken(null);
        userValidationRepository.save(usersValidationEntity);

        return usersRepository.save(user);
    }
}
