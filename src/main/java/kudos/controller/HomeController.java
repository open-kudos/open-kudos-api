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
    public String home(Model model, Principal principal) {
        if(principal != null) {
            User user = userDAO.getUserByEmail(principal.getName()).get();
            model.addAttribute("email",user.getEmail());
            model.addAttribute("username",user.getFirstName())
        }
        return "home";
    }

}
