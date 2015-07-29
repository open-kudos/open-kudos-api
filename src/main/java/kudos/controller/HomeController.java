package kudos.controller;

import com.google.common.base.Optional;
import kudos.dao.UserDAO;
import kudos.dao.UserInMemoryDAO;
import kudos.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

/**
 * Created by chc on 15.7.29.
 */
@Controller
@RequestMapping("/user")
public class HomeController {

    Logger LOG = Logger.getLogger(HomeController.class.getName());

    private final UserDAO userDAO;

    @Autowired
    public HomeController(UserDAO userDAO) {
        this.userDAO = userDAO;
        LOG.warn("HomeController has been created");
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
            LOG.warn("surname is: "+user.getLastName());

        return "home";
    }

    @RequestMapping(value="/home", method = RequestMethod.POST)
    public String showLogin(){
        LOG.warn("Logging out");
        return "redirect:/";
    }

}
