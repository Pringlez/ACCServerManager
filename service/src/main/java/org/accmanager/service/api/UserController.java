package org.accmanager.service.api;

import jakarta.mail.AuthenticationFailedException;
import jakarta.servlet.http.HttpServletResponse;
import org.accmanager.service.exception.IdentityServiceException;
import org.accmanager.service.entity.auth.UsersEntity;
import org.accmanager.service.services.auth.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;

import static org.accmanager.service.exception.IdentityServiceException.Reason.BAD_PASSWORD_RESET;
import static org.accmanager.service.exception.IdentityServiceException.Reason.BAD_TOKEN;

@Controller
@RequestMapping("/public")
public class UserController {

    static final String MESSAGE = "message";
    static final String ERROR = "error";
    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(value = "/ping", produces = "text/plain")
    @ResponseBody
    public String ping(@RequestParam(name = "debug", required = false) String debug) {
        if (debug != null && debug.length() > 0) {
            return "OK " + new Date() + " " + debug;
        }
        return "OK " + new Date();
    }

    @RequestMapping(path = "/logout")
    public RedirectView logout(HttpServletResponse response) {
        response.addHeader("HX-Redirect", "/");
        return new RedirectView("/?message=logout");
    }

    @PostMapping("/do-sign-in")
    public String doSignIn(@RequestParam(name = "error", defaultValue = "") String error, ModelMap modelMap) {
        if (error.length() > 0) {
            modelMap.addAttribute(ERROR, error);
        }
        return "index";
    }

    @GetMapping("/sign-in")
    public String signIn(@RequestParam(name = "error", defaultValue = "") String error, @RequestParam(name = "message", defaultValue = "") String message, ModelMap modelMap, HttpServletResponse response) {
        if (message.length() > 0) {
            modelMap.addAttribute(MESSAGE, message);
        }
        if (error.length() > 0) {
            modelMap.addAttribute(ERROR, "Invalid login, check your credentials!");
        }
        response.addHeader("HX-Redirect", "/");
        return "sign-in";
    }

    @GetMapping("/password-reset")
    public String passwordResetKey(@RequestParam(name = "token") String key, ModelMap modelMap) {
        modelMap.put("key", key);
        return "password-reset";
    }

    @PostMapping("/password-reset")
    public String updatePassword(@RequestParam(name = "key") String key, @RequestParam(name = "email") String email, @RequestParam(name = "password1") String password1,
                                 @RequestParam(name = "password2") String password2, ModelMap modelMap, HttpServletResponse response) {
        try {
            if (password1.compareTo(password2) != 0) {
                throw new IdentityServiceException(BAD_PASSWORD_RESET, "Passwords don't match!");
            }
            authService.updatePassword(email, key, password1);
            return signIn("", "Password successfully updated.", modelMap, response);
        } catch (IdentityServiceException e) {
            modelMap.addAttribute(MESSAGE, e.getMessage());
        }
        return signIn("", "", modelMap, response);
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String resetPassword(@RequestParam(name = "email", defaultValue = "") String email, ModelMap modelMap, HttpServletResponse response) {
        try {
            authService.requestPasswordReset(email);
            return signIn("", "Check your email for password reset link.", modelMap, response);
        } catch (IdentityServiceException e) {
            if (e.getReason().equals(BAD_TOKEN)) {
                modelMap.addAttribute(MESSAGE, "Unknown token.");
            } else {
                modelMap.addAttribute(MESSAGE, e.getMessage());
            }
        } catch (AuthenticationFailedException authenticationFailedException) {
            modelMap.addAttribute(MESSAGE, "Unable to send email right now, try again later.");
        }

        return "forgot-password";
    }

    @GetMapping("/sign-up")
    public String signUpPage(UsersEntity user) {
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(UsersEntity user, @RequestParam(name = "password-confirm") String confirm, ModelMap modelMap) {
        try {
            if (!user.getPassword().equals(confirm)) {
                throw new IdentityServiceException(IdentityServiceException.Reason.BAD_PASSWORD, "Passwords do not match!");
            }
            authService.signUpUser(user.getUsername(), user.getPassword(), false);
            return "redirect:/public/sign-in?message=Check%20your%20email%20to%20confirm%20your%20account%21";
        } catch (IdentityServiceException e) {
            modelMap.addAttribute(ERROR, e.getMessage());
        } catch (AuthenticationFailedException authenticationFailedException) {
            modelMap.addAttribute(ERROR, "Can't send email - email server is down or unreachable");
            authenticationFailedException.printStackTrace();
        }
        return "sign-up";
    }

    @GetMapping("/sign-up/confirm")
    public String confirmMail(@RequestParam("token") String token, ModelMap modelMap, HttpServletResponse response) {
        try {
            authService.confirmUser(token).orElseThrow(() -> new IdentityServiceException(BAD_TOKEN));
            return signIn("", "Email address confirmed!", modelMap, response);
        } catch (IdentityServiceException e) {
            return signIn("", "Unknown token.", modelMap, response);
        }
    }
}
