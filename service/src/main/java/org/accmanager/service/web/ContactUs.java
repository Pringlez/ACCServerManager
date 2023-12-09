package org.accmanager.service.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/public/contact-us")
public class ContactUs {

    @GetMapping
    public String start(Model model) {
        model.addAttribute("now", new Date().toInstant());
        return "contact-us";
    }
}
