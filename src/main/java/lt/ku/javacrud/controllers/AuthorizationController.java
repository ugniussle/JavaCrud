package lt.ku.javacrud.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthorizationController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
