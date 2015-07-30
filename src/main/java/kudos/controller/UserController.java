package kudos.controller;

import kudos.dao.UserDAO;
import kudos.model.UserForm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


/**
 * Created by chc on 15.7.23.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    private static Logger LOG = Logger.getLogger(UserController.class.getName());


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("form", new UserForm());
        return "registration";
    }


    @RequestMapping(value="/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("form") UserForm userForm, Errors errors) {
        LOG.warn("Status of errors before validation: " + errors.hasErrors());
        new UserForm.FormValidator().validate(userForm, errors);
        LOG.warn("Status of errors after validation: " + errors.hasErrors());
        if (!errors.hasErrors()) {

            try {
                userDAO.create(userForm.toUser());
                return "redirect:/user/registration-confirm";
            }
            catch(IllegalStateException e){
                errors.rejectValue("email",e.getMessage());
                return "registration";
            }

        }else {
            return "registration";
        }
    }

    @RequestMapping(value="/registration-confirm", method = RequestMethod.GET)
    public String registrationConfirm() {
        return "registration-confirm";
    }

    @RequestMapping(value="/login-with-facebook", method = RequestMethod.GET)
    public String loginWithFacebook() {



        return "login-with-facebook";
    }

}
