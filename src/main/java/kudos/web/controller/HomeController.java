package kudos.web.controller;

import com.google.common.base.Strings;
import kudos.dao.UserInMemoryDAO;
import kudos.model.User;
import kudos.model.UserForm;
import kudos.model.Validator;
import kudos.web.model.ErrorResponse;
import kudos.web.model.IndexResponse;
import kudos.web.model.LoginResponse;
import kudos.web.model.Response;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * Created by chc on 15.7.29.
 */
@Controller
public class HomeController extends BaseController {

    Logger LOG = Logger.getLogger(HomeController.class.getName());


    /*@RequestMapping(value="/home", method = RequestMethod.GET)
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
    }*/

    @RequestMapping(value="/", method = RequestMethod.GET)
    public Response index(Principal principal) {
        IndexResponse response = new IndexResponse();
        response.setIsLogged(principal != null);
        return response;
    }


    @RequestMapping(value="/login", method = RequestMethod.POST)
    public Response login(@RequestParam String email,
                          @RequestParam String password, HttpServletRequest request) {

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        } catch (AuthenticationException e) {
            return LoginResponse.fail(e.getMessage());
        }

        return LoginResponse.success();
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public Response register(@ModelAttribute("form") UserForm userForm, Errors errors){
        new UserForm.FormValidator().validate(userForm, errors);

        if(!errors.hasErrors()) {
            return LoginResponse.success();
        } else {
            return LoginResponse.fail("somewhat gone wrong");
        }
    }
    /*
    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "error", required = false) String error,
                       Model model) {
        LOG.warn("login method has been called");
        if(error != null){
            LOG.warn("there is error that must be add, it is: "+error.toString());
            model.addAttribute("error","The email and (or) username are incorrect!");
        }
        return "login";
    }*/

}
