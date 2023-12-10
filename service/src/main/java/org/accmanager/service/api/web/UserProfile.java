package org.accmanager.service.api.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/web/profile")
public class UserProfile {

    @Value("${spring.thymeleaf.darkMode:false}")
    private boolean darkMode;

    private static final String IS_DARK_MODE = "isDarkMode";

    @GetMapping("/details")
    public String index(Model model) {
        model.addAttribute("now", new Date());
        model.addAttribute(IS_DARK_MODE, darkMode);
        model.addAttribute("nickname", "Backend Populated");
        return "pages/profile/user-profile";
    }

    @GetMapping(path = "/data", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String data() {
        return "<p>hi! %s </p>".formatted(new Date().toString());
    }
}
