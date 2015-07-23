package com.codetutr.controller;

import com.codetutr.model.User;
import com.codetutr.model.Verificator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by chc on 15.7.22.
 */
@Controller
public class LoginController {

    @RequestMapping(value = "loginButton", method = RequestMethod.POST)
    public String authenticate(@ModelAttribute("email") String email,
                              @ModelAttribute("password") String password, Model model) {
            User user = Verificator.getUser(email);
            if(user != null && user.getUserPassword().equals(password)) {
                return "home";
            } else if(user != null){
                model.addAttribute("loginError", "Error logging in. Password is wrong");
                return "login";
            }
        model.addAttribute("loginError", "Error logging in. Please register");
        return "login";
    }

    @RequestMapping(value = "/login")
    public String showLogin() {
        return "login";
    }

}