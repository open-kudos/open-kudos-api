package com.codetutr.controller;

import com.codetutr.model.User;
import com.codetutr.model.UserForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by chc on 15.7.23.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public String registration() {
        return "registration";
    }


    @RequestMapping(value="/registration", method = RequestMethod.POST)
    public String register(@ModelAttribute("registration") @Valid UserForm userForm, Model model, Errors errors) {
        new UserForm.FormValidator().validate(userForm, errors);

        System.out.println(userForm.getEmail());
        System.out.println(userForm.getPassword());
        System.out.println(userForm.getConfirmPassword());

        model.addAttribute("form", userForm);

        final User user = userForm.toUser();

        if (USERS.containsKey(user.getEmail())) {
            errors.rejectValue("email", "email.occupied", "wrong");
        }

        if (errors.hasErrors()) {
            return "registration";
        }
        USERS.put(user.getEmail(), user);
        return "registration_confirm";
    }
}
