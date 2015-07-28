package kudos.controller;
;
import kudos.dao.UserDAO;
import kudos.model.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
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

    UserDAO userDAO;

    @Autowired
    public UserController (UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        System.out.println("started");
        model.addAttribute("form", new UserForm());
        return "registration";
    }


    @RequestMapping(value="registration", method = RequestMethod.POST)
    public String register(@ModelAttribute("form") @Valid UserForm userForm, Model model, Errors errors) {
        new UserForm.FormValidator().validate(userForm, errors);

        if (userDAO.getUserByEmail(userForm.getEmail()) != null || errors.hasFieldErrors()) {
            errors.rejectValue("email", "email.occupied");
            return "registration";
        }

        userDAO.create(userForm.toUser());
        return "redirect:/user/registration_confirm";
    }

}
