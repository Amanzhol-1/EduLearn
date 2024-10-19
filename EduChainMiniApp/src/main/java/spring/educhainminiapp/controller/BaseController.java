package spring.educhainminiapp.controller;

import jakarta.servlet.http.HttpSession;
import spring.educhainminiapp.model.User;

public abstract class BaseController {

    protected User getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("Пользователь не авторизован");
        }
        return user;
    }
}
