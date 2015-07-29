package kudos.controller;
;
import com.google.common.base.Optional;
import kudos.dao.UserDAO;
import kudos.model.User;
import kudos.model.UserForm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


/**
 * Created by chc on 15.7.23.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    private static Logger LOG = Logger.getLogger(UserController.class.getName());

    UserDAO userDAO;

    @Autowired
    public UserController (UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("form", new UserForm());
        return "registration";
    }


    @RequestMapping(value="/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("form") UserForm userForm, Errors errors) {
        new UserForm.FormValidator().validate(userForm, errors);

        try {
            userDAO.create(userForm.toUser());
        } catch (IllegalStateException e){
            errors.rejectValue("email","email.occupied");
            return "registration";
        }
        return "redirect:/user/registration-confirm";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam String email, @RequestParam String password) {
        return "redirect:/user/home";
    }

    @RequestMapping(value="/home", method = RequestMethod.GET)
    public String home(Model model, Principal principal) {
        LOG.warn(principal.getName());
        return "home";
    }

    @RequestMapping(value="/registration-confirm", method = RequestMethod.GET)
    public String registrationConfirm() {
        return "registration-confirm";
    }

}
