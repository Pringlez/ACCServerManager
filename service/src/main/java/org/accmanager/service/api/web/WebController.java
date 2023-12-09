package org.accmanager.service.api.web;

import jakarta.mail.AuthenticationFailedException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.accmanager.service.entity.auth.UsersEntity;
import org.accmanager.service.exception.IdentityServiceException;
import org.accmanager.service.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequestMapping("/web")
public class WebController implements ErrorController {

    @Value("${spring.thymeleaf.darkMode:false}")
    private boolean darkMode;

    private static final String MESSAGE = "message";
    private static final String ERROR = "error";
    private static final String IS_DARK_MODE = "isDarkMode";

    private final AuthService authService;

    public WebController(AuthService authService) {
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

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        model.addAttribute(IS_DARK_MODE, darkMode);
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == NOT_FOUND.value()) {
                return "pages/errors/error-404";
            } else if (statusCode == INTERNAL_SERVER_ERROR.value()) {
                return "pages/errors/error-500";
            }
        }
        return "pages/errors/error";
    }

    @PostMapping("/do-sign-in")
    public String doSignIn(@RequestParam(name = "error", defaultValue = "") String error, ModelMap modelMap) {
        if (error.length() > 0) {
            modelMap.addAttribute(ERROR, error);
        }
        return "index";
    }

    @GetMapping("/sign-in")
    public String signIn(@RequestParam(name = "error", defaultValue = "") String error, @RequestParam(name = "message", defaultValue = "") String message,
                         ModelMap modelMap, HttpServletResponse response) {
        modelMap.addAttribute(IS_DARK_MODE, darkMode);
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
    public String passwordResetKey(@RequestParam(name = "token") String key, ModelMap modelMap, Model model) {
        model.addAttribute(IS_DARK_MODE, darkMode);
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
    public String forgotPassword(Model model) {
        model.addAttribute(IS_DARK_MODE, darkMode);
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
    public String signUpPage(Model model) {
        model.addAttribute(IS_DARK_MODE, darkMode);
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(UsersEntity user, @RequestParam(name = "password-confirm") String confirm, ModelMap modelMap) {
        try {
            if (!user.getPassword().equals(confirm)) {
                throw new IdentityServiceException(IdentityServiceException.Reason.BAD_PASSWORD, "Passwords do not match!");
            }
            authService.signUpUser(user.getUsername(), user.getPassword(), false);
            return "redirect:/web/sign-in?message=Check%20your%20email%20to%20confirm%20your%20account%21";
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
