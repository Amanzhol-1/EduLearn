package spring.educhainminiapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import spring.educhainminiapp.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("firstName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("photoUrl", user.getPhotoUrl());
            return "home"; // Убедитесь, что файл home.html существует в templates
        } else {
            return "redirect:/login"; // Или перенаправление на страницу авторизации
        }
    }
}
