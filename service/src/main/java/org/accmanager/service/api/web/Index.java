package org.accmanager.service.api.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Controller
public class Index {

    @Value("${spring.thymeleaf.darkMode:false}")
    private boolean darkMode;

    @GetMapping("/")
    public String overview(Model model) {
        model.addAttribute("now", new Date());
        model.addAttribute("isDarkMode", darkMode);
        return "index";
    }
}
