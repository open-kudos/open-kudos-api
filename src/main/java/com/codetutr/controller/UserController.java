package com.codetutr.controller;

import com.codetutr.model.User;
import com.codetutr.model.UserForm;
import com.codetutr.model.Verificator;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.validation.Valid;

/**
 * Created by chc on 15.7.23.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        System.out.println("started");
        model.addAttribute("form", new UserForm());
        return "registration";
    }


    @RequestMapping(value="/registration", method = RequestMethod.POST)
    public String register(@ModelAttribute("form") @Valid UserForm userForm, Model model, Errors errors) {
        new UserForm.FormValidator().validate(userForm, errors);
        System.out.println("clicked");

        if(StringUtils.isEmpty(userForm.getEmail())) {
            errors.rejectValue("email", "required.email");
            return "registration";
        }

        if(!userForm.getEmail().matches(Verificator.EMAIL_PATTERN)){
            errors.rejectValue("email", "incorrect.email");
            return "registration";
        }

        System.out.println(userForm.getEmail());
        System.out.println(userForm.getPassword());
        System.out.println(userForm.getConfirmPassword());

        final User user = userForm.toUser();

        if (USERS.containsKey(user.getEmail())) {
            System.out.println("Email is already occupied");
            errors.rejectValue("email", "email.occupied");
            model.addAttribute("errors", errors);
            return "registration";
        }

        if(errors.hasErrors()){
            return "registration";
        }

        USERS.put(user.getEmail(), user);
            System.out.println("Registration was successful");
        return "registration_confirm";
    }

    @RequestMapping(value = "registration_confirm", method = RequestMethod.GET)
    public String showSuccessfullRegistrationView(@ModelAttribute("form") @Valid UserForm userForm, Model model){

        return "registration_confirm";
    }



}
