package org.accmanager.service.api.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/web/click-to-edit")
public class ClickToEdit {

    @Value("${spring.thymeleaf.darkMode:false}")
    private boolean darkMode;

    private static final String IS_DARK_MODE = "isDarkMode";

    @GetMapping
    public String start(Model model) {
        model.addAttribute("contact", Contact.demoContact());
        model.addAttribute("now", new Date().toInstant());
        model.addAttribute(IS_DARK_MODE, darkMode);
        return "pages/general/click-to-edit";
    }

    @PostMapping("/edit/{id}")
    public String editForm(Contact contact, Model model, @PathVariable String id) {
        model.addAttribute("contact", contact);
        model.addAttribute("id", id);
        model.addAttribute(IS_DARK_MODE, darkMode);
        return "pages/general/click-to-edit-form";
    }

    @PostMapping("/commit")
    public String editPost(Contact contact, Model model) {
        model.addAttribute("contact", contact);
        model.addAttribute(IS_DARK_MODE, darkMode);
        return "pages/general/click-to-edit-default";
    }
}
