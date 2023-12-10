package org.accmanager.service.api.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/web/todo")
public class ToDoList {

    @Value("${spring.thymeleaf.darkMode:false}")
    private boolean darkMode;

    private static final String IS_DARK_MODE = "isDarkMode";

    @GetMapping
    public String start(Model model) {
        model.addAttribute("now", new Date().toInstant());
        model.addAttribute("item", "Get Stuff Done");
        model.addAttribute(IS_DARK_MODE, darkMode);
        return "pages/general/todo";
    }

    @DeleteMapping(path = "/delete", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String delete() {
        return "";
    }

    /**
     * Thymeleaf will let you use the fragment syntax in a controller, as shown below.
     * https://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html#defining-and-referencing-fragments
     */
    @PostMapping(path = "/create")
    public String create(@RequestParam("new-todo") String todo, Model model) {
        model.addAttribute("item", todo);

        // Currently, IntelliJ doesn't recognize a Thymeleaf fragment returned in a controller.
        // https://youtrack.jetbrains.com/issue/IDEA-276625
        //
        //noinspection SpringMVCViewInspection
        return "pages/general/todo :: pages/general/todo";
    }
}
