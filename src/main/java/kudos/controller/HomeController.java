package kudos.controller;

import com.google.common.base.Optional;
import kudos.dao.UserDAO;
import kudos.dao.UserInMemoryDAO;
import kudos.model.User;
import kudos.model.UserForm;
import kudos.model.Validator;
import kudos.springconfig.SecurityConfig;
import org.apache.log4j.Logger;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;
import org.omg.SendingContext.RunTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

/**
 * Created by chc on 15.7.29.
 */
@Controller
public class HomeController {

    Logger LOG = Logger.getLogger(HomeController.class.getName());

    private final UserDAO userDAO;

    @Autowired
    public HomeController(UserDAO userDAO) {
        this.userDAO = userDAO;
        LOG.warn("HomeController has been created");
    }

    @RequestMapping(value="/home", method = RequestMethod.POST)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @RequestMapping(value="/home", method = RequestMethod.GET)
    public String showHome(Model model, Principal principal) {
            User user = userDAO.getUserByEmail(principal.getName()).get();

            model.addAttribute("email",user.getEmail());
            LOG.warn("username is: " + user.getEmail());

            model.addAttribute("name", user.getFirstName());
            LOG.warn("first name is: " + user.getFirstName());

            model.addAttribute("password", user.getPassword());
            LOG.warn("password is: " + user.getPassword());

            model.addAttribute("surname", user.getLastName());
            LOG.warn("surname is: " + user.getLastName());

        return "home";
    }
    

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "error", required = false) String error,
                       Model model) {
        LOG.warn("login method has been called");
        if(error != null){
            LOG.warn("there is error that must be add, it is: "+error.toString());
            model.addAttribute("error","The email and (or) username are incorrect!");
        }
        return "login";
    }

}
