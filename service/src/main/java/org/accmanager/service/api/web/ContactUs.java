package org.accmanager.service.api.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/web/contact-us")
public class ContactUs {

    @Value("${spring.thymeleaf.darkMode:false}")
    private boolean darkMode;

    @GetMapping
    public String start(Model model) {
        model.addAttribute("now", new Date().toInstant());
        model.addAttribute("isDarkMode", darkMode);
        return "contact-us";
    }
}
